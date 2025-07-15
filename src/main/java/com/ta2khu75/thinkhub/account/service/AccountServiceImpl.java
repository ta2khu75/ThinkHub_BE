package com.ta2khu75.thinkhub.account.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.entity.AccountProfile;
import com.ta2khu75.thinkhub.account.entity.AccountStatus;
import com.ta2khu75.thinkhub.account.mapper.AccountMapper;
import com.ta2khu75.thinkhub.account.projection.Author;
import com.ta2khu75.thinkhub.account.repository.AccountProfileRepository;
import com.ta2khu75.thinkhub.account.repository.AccountRepository;
import com.ta2khu75.thinkhub.account.repository.AccountStatusReporitory;
import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountSearch;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.account.response.AccountProfileResponse;
import com.ta2khu75.thinkhub.account.response.AccountResponse;
import com.ta2khu75.thinkhub.account.response.AccountStatusResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.follow.FollowDirection;
import com.ta2khu75.thinkhub.follow.FollowService;
import com.ta2khu75.thinkhub.follow.dto.FollowResponse;
import com.ta2khu75.thinkhub.follow.dto.FollowStatusResponse;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
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
		implements AccountService {

	AccountProfileRepository profileRepository;
	AccountStatusReporitory statusRepository;
	PasswordEncoder passwordEncoder;
	RedisService redisService;
	RoleService roleService;
	FollowService followService;

	public AccountServiceImpl(AccountRepository repository, AccountMapper mapper,
			AccountProfileRepository profileRepository, AccountStatusReporitory statusRepository,
			PasswordEncoder passwordEncoder, RedisService redisService, RoleService roleService,
			FollowService followService) {
		super(repository, mapper);
		this.profileRepository = profileRepository;
		this.statusRepository = statusRepository;
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
		this.roleService = roleService;
		this.followService = followService;
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
		if (!roleService.exists(status.getRoleId()))
			throw new NotFoundException("Could not find role with id: " + status.getRoleId());
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
		repository.deleteById(id);
	}

	@Override
	public AccountStatusResponse updateStatus(Long accountId, AccountStatusRequest request) {
		AccountStatus status = FunctionUtil.findOrThrow(accountId, AccountStatus.class,
				statusRepository::findByAccountId);
		mapper.update(request, status);
		if (!status.getRoleId().equals(request.roleId())) {
			if (roleService.exists(request.roleId()))
				status.setRoleId(request.roleId());
			else
				throw new NotFoundException("Could not find role with id: " + request.roleId());
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
//		Page<Account> page = repository.search(search);
//		return mapper.toPageResponse(page);
		return null;
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
	public void changePassword(ChangePasswordRequest request) {
		if (!request.newPassword().equals(request.confirmPassword()))
			throw new MismatchException("New password and confirm password not matches");
		Account account = this.readEntity(SecurityUtil.getCurrentAccountIdDecode());
		if (!passwordEncoder.matches(request.password(), account.getPassword()))
			throw new MismatchException("Password not matches");
		account.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(account);
	}

	@Override
	public void register(RegisterRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new MismatchException("password and confirm password not matches");
		if (repository.existsByEmail(request.email()))
			throw new AlreadyExistsException("Email already exists");
		RoleResponse role = roleService.readByName(RoleDefault.USER.name());
		AccountProfile profile = mapper.toEntity(request.profile());
		profile.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
		AccountStatus status = new AccountStatus();
		status.setRoleId(role.id());
		Account account = new Account();
		account.setEmail(request.email().toLowerCase());
		account.setUsername(request.username().toLowerCase());
		account.setPassword(passwordEncoder.encode(request.password()));
		account.setProfile(profile);
		account.setStatus(status);
		try {
			repository.save(account);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new AlreadyExistsException(e.getMessage());
		}
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
	public List<AuthorResponse> readAllAuthorsByAccountIds(List<Long> accountIds) {
		return repository.findAllAuthorsByAccountIds(accountIds).stream().map(mapper::toAuthorResponse).toList();
	}

	@Override
	@Transactional
	public void follow(Long followingId) {
		if (repository.existsById(followingId)) {
			followService.follow(followingId);
		}
		throw new NotFoundException("Could not find account with id: " + followingId);
	}

	@Override
	@Transactional
	public void unFollow(Long followingId) {
		followService.unFollow(followingId);
	}

	@Override
	@Transactional
	public FollowStatusResponse isFollowing(Long followingId) {
		return followService.isFollowing(followingId);
	}

	@Override
	public PageResponse<AuthorResponse> readFollow(Long following, FollowDirection direction, Search search) {
		PageResponse<FollowResponse> pageResponse = followService.readPage(following, direction, search);
		List<Long> accountIds = pageResponse.getContent().stream().map(followResponse -> followResponse.id()).toList();
		List<AuthorResponse> authors = readAllAuthorsByAccountIds(accountIds);
		return new PageResponse<>(pageResponse.getPage(), pageResponse.getTotalElements(), pageResponse.getPage(),
				authors);
	}

}
