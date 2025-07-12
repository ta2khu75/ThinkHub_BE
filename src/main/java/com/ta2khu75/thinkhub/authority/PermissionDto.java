package com.ta2khu75.thinkhub.authority;

import org.springframework.web.bind.annotation.RequestMethod;

public record PermissionDto(Long id, String pattern, RequestMethod requestMethod) {

}
