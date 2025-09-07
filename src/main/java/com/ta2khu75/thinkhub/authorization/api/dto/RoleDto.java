package com.ta2khu75.thinkhub.authorization.api.dto;

import java.util.Set;

public record RoleDto(Long id, String name, Set<PermissionDto> permissions) {
}
