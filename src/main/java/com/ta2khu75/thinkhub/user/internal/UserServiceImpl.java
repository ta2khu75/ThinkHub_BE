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

import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.AlreadyExistsException;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.FunctionUtil;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.UserApi;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserDto;
import com.ta2khu75.thinkhub.user.api.dto.UserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserResponse;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusResponse;
import com.ta2khu75.thinkhub.user.internal.entity.User;
import com.ta2khu75.thinkhub.user.internal.entity.UserStatus;
import com.ta2khu75.thinkhub.user.internal.mapper.UserMapper;
import com.ta2khu75.thinkhub.user.internal.repository.UserRepository;
import com.ta2khu75.thinkhub.user.internal.repository.UserStatusRepository;
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl extends BaseService<User, Long, UserRepository, UserMapper> implements UserApi {
	public UserServiceImpl(UserRepository repository, UserMapper mapper, UserStatusRepository statusRepository,
			AuthzApi authzApi, ApplicationEventPublisher events, RedisService redisService) {
		super(repository, mapper);
		this.statusRepository = statusRepository;
		this.redisService = redisService;
		this.authzApi= authzApi;
		this.events = events;
	}

	AuthzApi authzApi;
	RedisService redisService;
	UserStatusRepository statusRepository;
	ApplicationEventPublisher events;

	@Override
	public UserResponse create(CreateUserRequest request) {
		if (repository.existsByEmail(request.email().toLowerCase()))
			throw new AlreadyExistsException("Email already exists");
		User user = mapper.toEntity(request);
		user.setUsername(request.firstName() + " " + request.lastName());
		UserStatus status = mapper.toEntity(request.status());
		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, status.getRoleId()));
		user.setStatus(status);
		try {
			user = repository.save(user);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new AlreadyExistsException(e.getMessage());
		}
		return mapper.convert(user);
	}

	@Override
	public UserResponse update(Long userId, UserRequest request) {
		if (!SecurityUtil.getCurrentUserIdDecode().equals(userId))
			throw new InvalidDataException("You can't update other's user");
		User user = this.readEntity(userId);
		mapper.update(request, user);
		repository.save(user);
		return mapper.convert(user);
	}

	@Override
	public UserResponse read(Long userId) {
		User user = this.readEntity(userId);
		return mapper.convert(user);
	}

	@Override
	public void delete(Long id) {
		UserStatus status = readStatusByUserId(id);
		status.setDeleted(true);
		statusRepository.save(status);
	}

	@Override
	public UserResponse readByEmail(String email) {
		User user = repository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Could not find account with email: " + email));
		return mapper.convert(user);
	}

	private UserStatus readStatusByUserId(Long userId) {
		return FunctionUtil.findOrThrow(userId, UserStatus.class, statusRepository::findByUserId);
	}

	@Override
	public UserStatusResponse updateStatus(Long userId, UserStatusRequest request) {
		UserStatus status = readStatusByUserId(userId);
		mapper.update(request, status);
		if (!status.getRoleId().equals(request.roleId())) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, request.roleId()));
			status.setRoleId(request.roleId());
		}
		status = statusRepository.save(status);
		if (status.isNonLocked()) {
			redisService.delete(RedisKeyBuilder.userLock(userId));
		} else {
			redisService.setValue(RedisKeyBuilder.userLock(userId), "");
		}
		return mapper.toResponse(status);
	}

	@Override
	public PageResponse<UserResponse> search(UserSearch search) {
		Page<User> page = repository.search(search);
		return mapper.toPageResponse(page);
	}

	@Override
	public UserDto readDtoByEmail(String email) {
		return repository.findByEmail(email).map(mapper::toDto)
				.orElseThrow(() -> new NotFoundException("Could not find account with email: " + email));
	}

	@Override
	public UserDto readDto(Long id) {
		User user = this.readEntity(id);
		return mapper.toDto(user);
	}

	@Override
	public UserDto readDtoByUsername(String username) {
		return repository.findByUsername(username).map(mapper::toDto)
				.orElseThrow(() -> new NotFoundException("Could not find account with username: " + username));
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		Author author = repository.findAuthorByUserId(id)
				.orElseThrow(() -> new NotFoundException("Could not find profile with id: " + id));
		return mapper.toAuthorResponse(author);
	}

	@Override
	public Map<Long, AuthorResponse> readMapAuthorsByUserIds(Collection<Long> userIds) {
		return repository.findAllAuthorsByUserIds(userIds).stream()
				.collect(Collectors.toMap(Author::id, mapper::toAuthorResponse));
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
	public List<Long> readUserIdsByRoleName(String roleName) {
		RoleResponse role = authzApi.readRoleByName(roleName);
		return repository.findUserIdsByRoleId(role.id());
	}

	@Override
	public UserDto createDto(CreateUserRequest request) {
		if (repository.existsByEmail(request.email().toLowerCase()))
			throw new AlreadyExistsException("Email already exists");
		User user = mapper.toEntity(request);
		user.setUsername(request.firstName() + " " + request.lastName());
		UserStatus status = mapper.toEntity(request.status());
		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, status.getRoleId()));
		user.setStatus(status);
		try {
			user = repository.save(user);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			throw new AlreadyExistsException(e.getMessage());
		}
		return mapper.toDto(user);
	}

}
