package com.ta2khu75.thinkhub.shared.api.dto;

import java.io.Serializable;
import java.time.Instant;

public interface BaseRecordResponse <ID extends Serializable>{
	ID id();
	Instant createdAt();
	Instant updatedAt();
}
