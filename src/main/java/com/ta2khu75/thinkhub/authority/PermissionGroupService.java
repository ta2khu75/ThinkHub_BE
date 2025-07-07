package com.ta2khu75.thinkhub.authority;

import java.util.List;

import com.ta2khu75.thinkhub.authority.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionGroupService extends CrudService<PermissionGroupRequest, PermissionGroupResponse, Integer> {
	List<PermissionGroupResponse> readAll();
}
