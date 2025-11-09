package com.ta2khu75.thinkhub.authz.api.dto;

import java.util.Set;

public record RoleSummary(Long id, String name, Set<PermissionSummary> permissions) {
}
