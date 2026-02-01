package com.ta2khu75.thinkhub.modules.authz.api.dto.response;

import java.util.List;

public record PermissionGroupResponse(Integer id, String code, String name, String description,
		List<PermissionResponse> permissions) {

}
