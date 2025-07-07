package com.ta2khu75.thinkhub.authority.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(@NotBlank String name, Set<Long> permissionIds) {
}
