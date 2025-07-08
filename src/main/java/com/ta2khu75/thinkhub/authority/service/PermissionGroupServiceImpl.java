package com.ta2khu75.thinkhub.authority.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.authority.PermissionGroupService;
import com.ta2khu75.thinkhub.authority.entity.Permission;
import com.ta2khu75.thinkhub.authority.entity.PermissionGroup;
import com.ta2khu75.thinkhub.authority.mapper.PermissionGroupMapper;
import com.ta2khu75.thinkhub.authority.repository.PermissionGroupRepository;
import com.ta2khu75.thinkhub.authority.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionGroupResponse;
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
		return mapper.toResponse(permissionGroup);
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
		return mapper.toResponse(permissionGroup);
	}

	@Override
	public PermissionGroupResponse read(Integer id) {
		PermissionGroup permissionGroup = this.readEntity(id);
		return mapper.toResponse(permissionGroup);
	}

	@Override
	public void delete(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public List<PermissionGroupResponse> readAll() {
		return repository.findAll().stream().map(mapper::toResponse).toList();
	}

	@Override
	public Optional<PermissionGroupResponse> findByName(String name) {
		return repository.findByName(name).map(mapper::toResponse);
	}
}
