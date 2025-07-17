package com.ta2khu75.thinkhub.authority.dto;

import java.util.Set;

import com.ta2khu75.thinkhub.authority.PermissionDto;

public record RoleDto(Long id, String name, Set<PermissionDto> permissions) {
}
