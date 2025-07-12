package com.ta2khu75.thinkhub.account.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.account.AccountDto;
import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.entity.AccountProfile;
import com.ta2khu75.thinkhub.account.entity.AccountStatus;
import com.ta2khu75.thinkhub.account.mapper.AccountMapper;
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
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.ExistingException;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.exception.NotMatchesException;
import com.ta2khu75.thinkhub.shared.service.BaseConvertService;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.IdConverterService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.FunctionUtil;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl extends BaseConvertService<Account, Long, AccountRepository, AccountMapper>
		implements AccountService {
	AccountProfileRepository profileRepository;
	AccountStatusReporitory statusRepository;
	PasswordEncoder passwordEncoder;
	RedisService redisService;
	RoleService roleService;

	public AccountServiceImpl(AccountRepository repository, AccountMapper mapper, IdConverterService converter,
			AccountProfileRepository profileRepository, AccountStatusReporitory statusRepository,
			PasswordEncoder passwordEncoder, RedisService redisService, RoleService roleService) {
		super(repository, mapper, converter);
		this.profileRepository = profileRepository;
		this.statusRepository = statusRepository;
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
		this.roleService = roleService;
	}

//	public AccountServiceImpl(AccountRepository repository, AccountMapper mapper,
//			AccountProfileRepository profileRepository, AccountStatusReporitory statusRepository,
//			PasswordEncoder passwordEncoder, RedisService redisService, RoleService roleService) {
//		super(repository, mapper);
//		this.profileRepository = profileRepository;
//		this.statusRepository = statusRepository;
//		this.passwordEncoder = passwordEncoder;
//		this.redisService = redisService;
//		this.roleService = roleService;
//	}

	@Override
	public AccountResponse create(AccountRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new NotMatchesException("password and confirm password not matches");
		if (repository.existsByEmail(request.email().toLowerCase()))
			throw new ExistingException("Email already exists");
		AccountProfile profile = mapper.toEntity(request.profile());
		profile.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
		AccountStatus status = mapper.toEntity(request.status());
		if (!roleService.exists(status.getRoleId()))
			throw new NotFoundException("Could not find role with id: " + status.getRoleId());
		Account account = new Account();
		account.setEmail(request.email().toLowerCase());
		account.setPassword(passwordEncoder.encode(request.password()));
		account.setProfile(profile);
		account.setStatus(status);
		try {
			account = repository.save(account);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new ExistingException(e.getMessage());
		}
		return mapper.toResponse(account);
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
		if (!this.decodeAccountId(SecurityUtil.getCurrentAccountId()).equals(accountId))
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
		return mapper.toResponse(account);
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
			throw new NotMatchesException("New password and confirm password not matches");
		Account account = this.readEntity(this.decodeAccountId(SecurityUtil.getCurrentAccountId()));
		if (!passwordEncoder.matches(request.password(), account.getPassword()))
			throw new NotMatchesException("Password not matches");
		account.setPassword(passwordEncoder.encode(request.newPassword()));
		account = repository.save(account);
	}

	@Override
	public void register(RegisterRequest request) {
		if (!request.password().equals(request.confirmPassword()))
			throw new NotMatchesException("password and confirm password not matches");
		if (repository.existsByEmail(request.email()))
			throw new ExistingException("Email already exists");
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
			account = repository.save(account);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new ExistingException(e.getMessage());
		}
	}

	@Override
	public AccountDto readDtoByUsername(String username) {
		return repository.findByUsername(username).map(mapper::toDto)
				.orElseThrow(() -> new NotFoundException("Could not find account with username: " + username));
	}
}
