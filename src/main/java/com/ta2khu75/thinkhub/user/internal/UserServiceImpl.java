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

import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.exception.AlreadyExistsException;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.FunctionUtil;
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
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl extends BaseService<User, Long, UserRepository, UserMapper> implements UserApi, IdDecodable {

	public UserServiceImpl(UserRepository repository, UserMapper mapper, RedisService redisService,
			UserStatusRepository statusRepository, ApplicationEventPublisher events) {
		super(repository, mapper);
		this.redisService = redisService;
		this.statusRepository = statusRepository;
		this.events = events;
	}

	RedisService redisService;
	UserStatusRepository statusRepository;
	ApplicationEventPublisher events;

//	@Override
//	public UserSummary create(UserSummary summary) {
//		if (repository.existsByEmail(summary.email().toLowerCase()))
//			throw new AlreadyExistsException("Email already exists");
//		User user = mapper.toEntity(summary);
//		user.setUsername(summary.firstName() + " " + summary.lastName());
//		UserStatus status = mapper.toEntity(summary.status());
//		events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, status.getRoleId()));
//		user.setStatus(status);
//		try {
//			user = repository.save(user);
//		} catch (DataIntegrityViolationException e) {
//			e.printStackTrace();
//			throw new AlreadyExistsException(e.getMessage());
//		}
//		return mapper.toSummary(user);
//	}

	@Override
	public UserSummary create(UserCreateRequest request) {
		if (repository.existsByEmail(request.email().toLowerCase()))
			throw new AlreadyExistsException("Email already exists");
		User user = mapper.toEntity(request);
		if (user.getUsername() == null)
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
		return mapper.toSummary(user);
	}

	@Override
	public UserResponse update(String userId, UserRequest request) {
		Long id = decodeId(userId);
		if (!SecurityUtil.getCurrentUserIdDecode().equals(id))
			throw new InvalidDataException("You can't update other's user");
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
	public UserStatusResponse updateStatus(String userId, UserStatusRequest request) {
		Long id = decodeId(userId);
		UserStatus status = readStatusByUserId(id);
		mapper.update(request, status);
		if (!status.getRoleId().equals(request.roleId())) {
			events.publishEvent(new CheckExistsEvent<>(EntityType.ROLE, request.roleId()));
			status.setRoleId(request.roleId());
		}
		status = statusRepository.save(status);
		if (status.isNonLocked()) {
			redisService.delete(RedisKeyBuilder.userLock(id));
		} else {
			redisService.setValue(RedisKeyBuilder.userLock(id), "");
		}
		return mapper.toResponse(status);
	}

	@Override
	public PageResponse<UserResponse> search(UserSearch search) {
		Page<User> page = repository.search(search);
		return mapper.toPageResponse(page);
	}

	@Override
	public UserSummary readSummaryByEmail(String email) {
		return repository.findByEmail(email).map(mapper::toSummary)
				.orElseThrow(() -> new NotFoundException("Could not find account with email: " + email));
	}

	@Override
	public UserSummary readSummary(String userId) {
		Long id = decodeId(userId);
		User user = this.readEntity(id);
		return mapper.toSummary(user);
	}

	@Override
	public UserSummary readSummary(Long userId) {
		User user = this.readEntity(userId);
		return mapper.toSummary(user);
	}

	@Override
	public UserSummary readDtoByUsername(String username) {
		return repository.findByUsername(username).map(mapper::toSummary)
				.orElseThrow(() -> new NotFoundException("Could not find account with username: " + username));
	}

	@Override
	public AuthorResponse readAuthor(String userId) {
		Long id = decodeId(userId);
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
		Author author = repository.findAuthorByUserId(id)
				.orElseThrow(() -> new NotFoundException("Could not find profile with id: " + id));
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
}
