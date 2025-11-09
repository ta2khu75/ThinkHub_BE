package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.enums.EntityType;

public interface ExistsService<Id> {
	void ensureExists(Id id);

	EntityType getEntityType();
}