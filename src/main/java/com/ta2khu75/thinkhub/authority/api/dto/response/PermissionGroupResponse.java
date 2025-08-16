package com.ta2khu75.thinkhub.authority.api.dto.response;

import java.util.List;

public record PermissionGroupResponse(Integer id,String name, String description, List<PermissionResponse> permissions) {
	
}
