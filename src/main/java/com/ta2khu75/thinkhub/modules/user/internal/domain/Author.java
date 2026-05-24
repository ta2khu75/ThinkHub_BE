package com.ta2khu75.thinkhub.modules.user.internal.domain;

import com.ta2khu75.thinkhub.shared.domain.entity.HasPublicId;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

public record Author(Long id, String displayName) implements HasPublicId {

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.USER;
	}

	@Override
	public Long getId() {
		return this.id;
	}
}
