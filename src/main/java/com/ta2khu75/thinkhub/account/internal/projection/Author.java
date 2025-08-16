package com.ta2khu75.thinkhub.account.internal.projection;

import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.entity.IdEntity;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

public record Author(Long id, String displayName) implements IdConfigProvider, IdEntity<Long> {

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.ACCOUNT;
	}

	@Override
	public Long getId() {
		return this.id;
	}
}
