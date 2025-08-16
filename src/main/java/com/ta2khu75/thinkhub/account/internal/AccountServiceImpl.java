package com.ta2khu75.thinkhub.account.internal;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.account.api.AccountApi;
import com.ta2khu75.thinkhub.account.api.dto.AccountDto;
import com.ta2khu75.thinkhub.account.api.dto.UpdatePassword;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountRequest;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountSearch;
import com.ta2khu75.thinkhub.account.api.dto.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountResponse;
import com.ta2khu75.thinkhub.account.api.dto.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.account.internal.entity.Account;
import com.ta2khu75.thinkhub.account.internal.entity.AccountProfile;
import com.ta2khu75.thinkhub.account.internal.entity.AccountStatus;
import com.ta2khu75.thinkhub.account.internal.mapper.AccountMapper;
import com.ta2khu75.thinkhub.account.internal.projection.Author;
import com.ta2khu75.thinkhub.account.internal.repository.AccountProfileRepository;
import com.ta2khu75.thinkhub.account.internal.repository.AccountRepository;
import com.ta2khu75.thinkhub.account.internal.repository.AccountStatusRepository;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.AlreadyExistsException;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.exception.MismatchException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.FunctionUtil;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl extends BaseService<Account, Long, AccountRepository, AccountMapper>
		implements AccountApi {

	AccountProfileRepository profileRepository;
	AccountStatusRepository statusRepository;
	ApplicationEventPublisher events;
	PasswordEncoder passwordEncoder;
	RedisService redisService;

	public AccountServiceImpl(AccountRepository repository, AccountMapper mapper,
			AccountProfileRepository profileRepository, AccountStatusRepository statusRepository,
			PasswordEncoder passwordEncoder, RedisService redisService,
			ApplicationEventPublisher events) {
		super(repository, mapper);
		this.profileRepository = profileRepository;
		this.statusRepository = statusRepository;
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
		this.events = events;
	}

	@Override
	public AccountResponse create(AccountRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new MismatchException("password and confirm password not matches");
		if (repository.existsByEmail(request.email().toLowerCase()))
			throw new AlreadyExistsException("Email already exists");
		AccountProfile profile = mapper.toEntity(request.profile());
		profile.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
		AccountStatus status = mapper.toEntity(request.status());
		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, status.getRoleId()));
		Account account = new Account();
		account.setUsername(request.username().toLowerCase());
		account.setEmail(request.email().toLowerCase());
		account.setPassword(passwordEncoder.encode(request.password()));
		account.setProfile(profile);
		account.setStatus(status);
		try {
			account = repository.save(account);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new AlreadyExistsException(e.getMessage());
		}
		return mapper.convert(account);
	}

	@Override
	public AccountProfileResponse readProfile(Long accountId) {
		AccountProfile profile = FunctionUtil.findOrThrow(accountId, AccountProfile.class,
				profileRepository::findByAccountId);
		return mapper.toResponse(profile);
	}

	@Override
	public void delete(Long id) {
		AccountStatus status = FunctionUtil.findOrThrow(id, AccountStatus.class, statusRepository::findByAccountId);
		status.setDeleted(true);
		statusRepository.save(status);
	}

	@Override
	public AccountStatusResponse updateStatus(Long accountId, AccountStatusRequest request) {
		AccountStatus status = FunctionUtil.findOrThrow(accountId, AccountStatus.class,
				statusRepository::findByAccountId);
		mapper.update(request, status);
		if (!status.getRoleId().equals(request.roleId())) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, request.roleId()));
			status.setRoleId(request.roleId());
		}
		status = statusRepository.save(status);
		if (status.isNonLocked()) {
			redisService.delete(RedisKeyBuilder.accountLock(accountId));
		} else {
			redisService.setValue(RedisKeyBuilder.accountLock(accountId), "");
		}
		return mapper.toResponse(status);
	}

	@Override
	public AccountProfileResponse updateProfile(Long accountId, AccountProfileRequest request) {
		if (!SecurityUtil.getCurrentAccountIdDecode().equals(accountId))
			throw new InvalidDataException("You can't update other's profile");
		AccountProfile profile = FunctionUtil.findOrThrow(accountId, AccountProfile.class,
				profileRepository::findByAccountId);
		mapper.update(request, profile);
		return mapper.toResponse(profile);
	}

	@Override
	public PageResponse<AccountResponse> search(AccountSearch search) {
		Page<Account> page = repository.search(search);
		return mapper.toPageResponse(page);
	}

	@Override
	public AccountResponse readByEmail(String email) {
		Account account = repository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Could not find account with email: " + email));
		return mapper.convert(account);
	}

	@Override
	public AccountDto readDtoByEmail(String email) {
		return repository.findByEmail(email).map(mapper::toDto)
				.orElseThrow(() -> new NotFoundException("Could not find account with email: " + email));
	}

	@Override
	public AccountDto readDto(Long id) {
		Account account = this.readEntity(id);
		return mapper.toDto(account);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public void updatePassword(UpdatePassword request) {
		Account account = this.readEntity(SecurityUtil.getCurrentAccountIdDecode());
		if (!passwordEncoder.matches(request.password(), account.getPassword()))
			throw new MismatchException("Password not matches");
		account.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(account);
	}

	@Override
	public AccountDto readDtoByUsername(String username) {
		return repository.findByUsername(username).map(mapper::toDto)
				.orElseThrow(() -> new NotFoundException("Could not find account with username: " + username));
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find category with id: " + id);
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ACCOUNT;
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		Author author = repository.findAuthorByAccountId(id)
				.orElseThrow(() -> new NotFoundException("Could not find profile with id: " + id));
		return mapper.toAuthorResponse(author);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByAccountIds(Collection<Long> accountIds) {
		return repository.findAllAuthorsByAccountIds(accountIds).stream()
				.collect(Collectors.toMap(Author::id, mapper::toAuthorResponse));
	}

}
