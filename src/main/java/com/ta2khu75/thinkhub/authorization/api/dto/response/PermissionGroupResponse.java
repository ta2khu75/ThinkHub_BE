package com.ta2khu75.thinkhub.authorization.api.dto.response;

import java.util.List;

public record PermissionGroupResponse(Integer id,String name, String description, List<PermissionResponse> permissions) {
	
}
