package com.ta2khu75.thinkhub.authority.api.dto.response;

import java.util.Set;

public record RoleResponse(Long id, String name, String description, Set<Long> permissionIds) {
}
