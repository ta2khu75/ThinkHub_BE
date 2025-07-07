package com.ta2khu75.thinkhub.authority.request;

import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequest (@NotBlank String summary, String description,@NotBlank String path, RequestMethod requestMethod) {

}
