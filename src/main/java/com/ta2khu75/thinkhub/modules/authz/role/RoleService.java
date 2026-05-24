package com.ta2khu75.thinkhub.modules.authz.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.modules.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.modules.authz.permission.Permission;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
class RoleService extends BaseService<Role, Long, RoleRepository> implements RoleApi {

	protected RoleService(RoleRepository repository, RoleMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private final RoleMapper mapper;

	@Override
	public RoleResponse create(@Valid RoleRequest request) {
		Role role = mapper.toEntity(request);
		role.setPermissions(request.permissionIds().stream().map(permissionId -> {
			Permission permission = new Permission();
			permission.setId(permissionId);
			return permission;
		}).collect(Collectors.toSet()));
		role = repository.save(role);
		return mapper.convert(role);
	}

	@Override
	@CacheEvict(value = { "role-summary" }, allEntries = true)
	public RoleResponse update(Long id, @Valid RoleRequest request) {
		Role role = this.readEntity(id);
		mapper.update(request, role);
		role.setPermissions(request.permissionIds().stream().map(permissionId -> {
			Permission permission = new Permission();
			permission.setId(permissionId);
			return permission;
		}).collect(Collectors.toSet()));
		role = repository.save(role);
		return mapper.convert(role);
	}

	@Override
	public RoleResponse read(Long id) {
		Role role = this.readEntity(id);
		return mapper.convert(role);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public RoleResponse readByName(String roleName) {
		Role role = repository.findByName(roleName).orElseThrow(
				() -> new NotFoundException(RoleErrorCode.NOT_FOUND, "Could not find Role with name: " + roleName));
		return mapper.convert(role);
	}

	@Override
	public List<RoleResponse> readAll() {
		return repository.findAll().stream().map(mapper::convert).toList();
	}

	@Override
	public boolean exists(Long id) {
		return repository.existsById(id);
	}

	@Override
	@Cacheable(value = "role-summary", key = "#name")
	public RoleSummary readSummaryByName(String name) {
		Role role = repository.findByName(name).orElseThrow(
				() -> new NotFoundException(RoleErrorCode.NOT_FOUND, "Could not find Role with name: " + name));
		return mapper.toSummary(role);
	}

	@Override
	@Transactional
	public RoleSummary readSummary(Long id) {
		Role role = this.readEntity(id);
		return mapper.toSummary(role);
	}

	@Override
	public boolean existsByName(String name) {
		return false;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ROLE;
	}

	@Override
	public void ensureExists(Long id) {
		this.assertExists(id);
	}

}
