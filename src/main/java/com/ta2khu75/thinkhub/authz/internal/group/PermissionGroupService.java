package com.ta2khu75.thinkhub.authz.internal.group;

import java.util.List;
import java.util.Optional;

import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionGroupService extends CrudService<PermissionGroupRequest, PermissionGroupResponse, Integer> {
	List<PermissionGroupResponse> readAll();
	Optional<PermissionGroupResponse> findByName(String name);
}
