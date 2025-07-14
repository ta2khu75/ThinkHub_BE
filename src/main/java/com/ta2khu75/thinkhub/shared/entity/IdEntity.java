package com.ta2khu75.thinkhub.shared.entity;

import java.io.Serializable;

public interface IdEntity<ID extends Serializable> {
	ID getId();
}
