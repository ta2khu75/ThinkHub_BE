package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.enums.EntityType;

public interface ExistsService<Id> extends EntityTypeSupport {
	void checkExists(Id id);
}

interface EntityTypeSupport {
	EntityType getEntityType();
}