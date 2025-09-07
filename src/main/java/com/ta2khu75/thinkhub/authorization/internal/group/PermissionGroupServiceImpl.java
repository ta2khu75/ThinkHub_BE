package com.ta2khu75.thinkhub.authorization.internal.group;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.authorization.api.dto.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authorization.internal.permission.Permission;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class PermissionGroupServiceImpl
		extends BaseService<PermissionGroup, Integer, PermissionGroupRepository, PermissionGroupMapper>
		implements PermissionGroupService {
	protected PermissionGroupServiceImpl(PermissionGroupRepository repository, PermissionGroupMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public PermissionGroupResponse create(@Valid PermissionGroupRequest request) {
		PermissionGroup permissionGroup = mapper.toEntity(request);
		permissionGroup.setPermissions(request.permissionIds().stream().map(permissionId -> {
			Permission permission = new Permission();
			permission.setId(permissionId);
			return permission;
		}).toList());
		permissionGroup = repository.save(permissionGroup);
		return mapper.convert(permissionGroup);
	}

	@Override
	public PermissionGroupResponse update(Integer id, @Valid PermissionGroupRequest request) {
		PermissionGroup permissionGroup = this.readEntity(id);
		mapper.update(request, permissionGroup);
		permissionGroup.setPermissions(request.permissionIds().stream().map(permissionId -> {
			Permission permission = new Permission();
			permission.setId(permissionId);
			return permission;
		}).collect(Collectors.toList()));
		permissionGroup = repository.save(permissionGroup);
		return mapper.convert(permissionGroup);
	}

	@Override
	public PermissionGroupResponse read(Integer id) {
		PermissionGroup permissionGroup = this.readEntity(id);
		return mapper.convert(permissionGroup);
	}

	@Override
	public void delete(Integer id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public List<PermissionGroupResponse> readAll() {
		return repository.findAll().stream().map(mapper::convert).toList();
	}

	@Override
	public Optional<PermissionGroupResponse> findByName(String name) {
		return repository.findByName(name).map(mapper::convert);
	}
}
