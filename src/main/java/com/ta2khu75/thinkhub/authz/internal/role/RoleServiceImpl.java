package com.ta2khu75.thinkhub.authz.internal.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.authz.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authz.internal.permission.Permission;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class RoleServiceImpl extends BaseService<Role, Long, RoleRepository, RoleMapper> implements RoleService {

	protected RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
		super(repository, mapper);
	}

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
		Role role = repository.findByName(roleName)
				.orElseThrow(() -> new NotFoundException("Could not find Role with name: " + roleName));
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
	public RoleDto readDtoByName(String name) {
		Role role = repository.findByName(name)
				.orElseThrow(() -> new NotFoundException("Could not find Role with name: " + name));
		return mapper.toDto(role);
	}

	@Override
	@Transactional
	public RoleDto readDto(Long id) {
		Role role = this.readEntity(id);
		return mapper.toDto(role);
	}

	@Override
	public boolean existsByName(String name) {
		return false;
	}

	@Override
	public void checkExists(Long id) {
		if (!repository.existsById(id)) {
			throw new NotFoundException("Could not find role with id: " + id);
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ROLE;
	}

}
