package com.ta2khu75.thinkhub.shared.service;

import org.springframework.transaction.annotation.Transactional;

public interface CrudService<REQ, RES, Id> {
	@Transactional
	RES create(REQ request);

	@Transactional
	RES update(Id id, REQ request);

	@Transactional(readOnly = true)
	RES read(Id id);

	@Transactional
	void delete(Id id);
}