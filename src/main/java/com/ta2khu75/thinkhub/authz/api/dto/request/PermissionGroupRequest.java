package com.ta2khu75.thinkhub.authz.api.dto.request;

import java.util.Set;

public record PermissionGroupRequest(String name, String description, Set<Long> permissionIds) {

}
