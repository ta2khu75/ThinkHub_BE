package com.ta2khu75.thinkhub.authority.internal;

import java.util.List;
import java.util.Optional;

import com.ta2khu75.thinkhub.authority.api.dto.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionGroupService extends CrudService<PermissionGroupRequest, PermissionGroupResponse, Integer> {
	List<PermissionGroupResponse> readAll();
	Optional<PermissionGroupResponse> findByName(String name);
}
