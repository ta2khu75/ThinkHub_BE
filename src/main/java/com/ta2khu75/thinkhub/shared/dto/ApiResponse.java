package com.ta2khu75.thinkhub.shared.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {
	Object data;
	Object error;
	String message;
	int status;
}
