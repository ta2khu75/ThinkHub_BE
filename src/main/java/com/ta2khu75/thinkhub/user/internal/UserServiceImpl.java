package com.ta2khu75.thinkhub.user.internal;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.domain.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.ConflictException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.UserApi;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;
import com.ta2khu75.thinkhub.user.internal.entity.User;
import com.ta2khu75.thinkhub.user.internal.entity.UserStatus;
import com.ta2khu75.thinkhub.user.internal.mapper.UserMapper;
import com.ta2khu75.thinkhub.user.internal.repository.UserRepository;
import com.ta2khu75.thinkhub.user.internal.repository.UserStatusRepository;
import com.ta2khu75.thinkhub.user.internal.service.UserService;
import com.ta2khu75.thinkhub.user.internal.validator.UserErrorCode;
import com.ta2khu75.thinkhub.user.internal.validator.UserValidator;
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl extends BaseService<User, Long, UserRepository> implements UserApi, UserService, IdDecodable {

	public UserServiceImpl(UserRepository repository, UserMapper mapper, UserValidator validator,
			RedisService redisService, UserStatusRepository statusRepository, ApplicationEventPublisher events) {
		super(repository);
		this.mapper = mapper;
		this.validator = validator;
		this.redisService = redisService;
		this.statusRepository = statusRepository;
		this.events = events;
	}

	UserMapper mapper;
	UserValidator validator;
	RedisService redisService;
	UserStatusRepository statusRepository;
	ApplicationEventPublisher events;

	@Override
	public UserSummary create(UserCreateRequest request) {
		boolean emailExists = repository.existsByEmail(request.email().toLowerCase());
		validator.validateCreate(request, emailExists);
		User user = mapper.toEntity(request);
		UserStatus status = mapper.toEntity(request.status());
		if (user.getUsername() == null)
			user.setUsername(request.firstName() + " " + request.lastName());
		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, status.getRoleId()));
		user.setStatus(status);
		try {
			user = repository.save(user);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new ConflictException(UserErrorCode.EMAIL_EXISTS, "Email already exists");
		}
		return mapper.toSummary(user);
	}

	@Override
	public UserResponse update(String userId, UserRequest request) {
		Long id = decodeId(userId);
		User user = this.readEntity(id);
		mapper.update(request, user);
		repository.save(user);
		return mapper.convert(user);
	}

	@Override
	public UserResponse read(String userId) {
		Long id = decodeId(userId);
		User user = this.readEntity(id);
		return mapper.convert(user);
	}

	@Override
	public void delete(String userId) {
		Long id = decodeId(userId);
		UserStatus status = readStatusByUserId(id);
		status.setDeleted(true);
		statusRepository.save(status);
	}

//	@Override
//	public UserResponse readByEmail(String email) {
//		User user = repository.findByEmail(email)
//				.orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND.name(),
//						"Could not find account with email: " + email));
//		return mapper.convert(user);
//	}

	private UserStatus readStatusByUserId(Long userId) {
		return statusRepository.findByUserId(userId).orElseThrow(
				() -> new NotFoundException(UserErrorCode.NOT_FOUND.name(), "Could not find user with id: " + userId));
	}

	@Override
	public UserStatusResponse updateStatus(String userId, UserStatusRequest request) {
		Long id = decodeId(userId);
		UserStatus status = readStatusByUserId(id);
		validator.validateUpdateStatus(status, request, id);
		mapper.update(request, status);
		if (validator.isRoleChanged(status, request)) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, request.roleId()));
			status.setRoleId(request.roleId());
		}
		status = statusRepository.save(status);
		syncUserLockCache(id, status.isNonLocked());
		return mapper.toResponse(status);
	}

	private void syncUserLockCache(Long userId, boolean nonLocked) {
		if (nonLocked) {
			redisService.delete(RedisKeyBuilder.userLock(userId));
		} else {
			redisService.setValue(RedisKeyBuilder.userLock(userId), "");
		}
	}

	@Override
	public PageResponse<UserResponse> search(UserSearch search) {
		Page<User> page = repository.search(search);
		return mapper.toPageResponse(page);
	}

	@Override
	public UserSummary readSummaryByEmail(String email) {
		return repository.findByEmail(email).map(mapper::toSummary)
				.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND.name(),
						"Could not find account with email: " + email));
	}

	@Override
	public UserSummary readSummary(String userId) {
		Long id = decodeId(userId);
		User user = this.readEntity(id);
		return mapper.toSummary(user);
	}

	@Override
	public AuthorResponse readAuthor(String userId) {
		Long id = decodeId(userId);
		Author author = repository.findAuthorByUserId(id).orElseThrow(
				() -> new NotFoundException(UserErrorCode.NOT_FOUND.name(), "Could not find profile with id: " + id));
		return mapper.toAuthorResponse(author);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds) {
		return repository.findAllAuthorsByUserIds(userIds).stream()
				.collect(Collectors.toMap(Author::id, mapper::toAuthorResponse));
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.USER;
	}

	@Override
	public List<Long> readAllUserIdByRoleId(Long id) {
		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, id));
		return repository.findAllUserIdByRoleId(id);
	}

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		Author author = repository.findAuthorByUserId(id).orElseThrow(
				() -> new NotFoundException(UserErrorCode.NOT_FOUND.name(), "Could not find profile with id: " + id));
		return mapper.toAuthorResponse(author);
	}

	@Override
	public void ensureExists(Long id) {
		this.assertExists(id);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public UserResponse readMe() {
		Long userId = SecurityUtil.getCurrentUserIdDecode();
		User user = this.readEntity(userId);
		return mapper.convert(user);
	}

	@Override
	public UserResponse updateMe(UserRequest request) {
		Long userId = SecurityUtil.getCurrentUserIdDecode();
		User user = this.readEntity(userId);
		mapper.update(request, user);
		repository.save(user);
		return mapper.convert(user);
	}
}
