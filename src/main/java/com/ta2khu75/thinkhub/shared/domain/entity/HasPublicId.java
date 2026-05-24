package com.ta2khu75.thinkhub.shared.domain.entity;

import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

public interface HasPublicId {
	IdSubject getIdSubject();

	Long getId();
}
