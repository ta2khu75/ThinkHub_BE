package com.ta2khu75.thinkhub.user.projection.internal.projection;

import com.ta2khu75.thinkhub.shared.domain.entity.HasIdSubject;
import com.ta2khu75.thinkhub.shared.domain.entity.IdEntity;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

public record Author(Long id, String displayName) implements HasIdSubject, IdEntity<Long> {

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.USER;
	}

	@Override
	public Long getId() {
		return this.id;
	}
}
