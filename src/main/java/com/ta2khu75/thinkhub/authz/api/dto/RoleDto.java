package com.ta2khu75.thinkhub.authz.api.dto;

import java.util.Set;

public record RoleDto(Long id, String name, Set<PermissionDto> permissions) {
}
