package com.ta2khu75.thinkhub.authority.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(@NotBlank String name, String description, Set<Long> permissionIds) {
}
