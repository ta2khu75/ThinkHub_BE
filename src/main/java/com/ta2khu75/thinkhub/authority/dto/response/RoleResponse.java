package com.ta2khu75.thinkhub.authority.dto.response;

import java.util.Set;

public record RoleResponse(Long id, String name, Set<Long> permissionIds) {
}
