package com.ta2khu75.thinkhub.shared.api.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.Data;
@Data
public class BaseClassResponse<ID extends Serializable> {
	private ID id;
	private Instant createdAt;
	private Instant updatedAt;
}
