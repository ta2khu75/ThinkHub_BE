package com.ta2khu75.thinkhub.authz.api.dto;

import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionSummary {
	Long id;
	@NotBlank
	String code;
	@NotBlank
	String name;
	String description;
	@NotBlank
	String pattern;
	@NotNull
	RequestMethod method;
}
