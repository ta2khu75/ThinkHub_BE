package com.ta2khu75.thinkhub.shared.api.dto;

import java.io.Serializable;
import java.time.Instant;

public interface BaseClassResponse<ID extends Serializable> {
	ID getId();
	Instant getCreatedAt();
	Instant getUpdatedAt();
}
