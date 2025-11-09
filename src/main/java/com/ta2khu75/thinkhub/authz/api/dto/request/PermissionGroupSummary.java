package com.ta2khu75.thinkhub.authz.api.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionGroupSummary {
	Integer id;
	@NotBlank
	String code;
	@NotBlank
	String name;
	String description;
	@NotBlank
	Set<Long> permissionIds;

}
