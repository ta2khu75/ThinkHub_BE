package com.ta2khu75.thinkhub.shared.dto;

import java.io.Serializable;
import java.time.Instant;

public interface BaseResponse <ID extends Serializable>{
	ID id();
	Instant createdAt();
	Instant updatedAt();
}
