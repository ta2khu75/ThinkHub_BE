package com.ta2khu75.thinkhub.authz.api.dto;

import org.springframework.web.bind.annotation.RequestMethod;

public record PermissionDto(Long id, String pattern, RequestMethod method) {}
