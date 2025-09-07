package com.ta2khu75.thinkhub.user.projection.internal.projection;

import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.entity.IdEntity;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

public record Author(Long id, String displayName) implements IdConfigProvider, IdEntity<Long> {

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public Long getId() {
		return this.id;
	}
}
