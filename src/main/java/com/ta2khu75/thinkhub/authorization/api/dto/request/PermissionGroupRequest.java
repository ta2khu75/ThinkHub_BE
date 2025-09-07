package com.ta2khu75.thinkhub.authorization.api.dto.request;

import java.util.Set;

public record PermissionGroupRequest(String name, String description, Set<Long> permissionIds) {

}
