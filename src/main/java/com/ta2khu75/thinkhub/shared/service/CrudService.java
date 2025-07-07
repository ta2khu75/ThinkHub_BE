package com.ta2khu75.thinkhub.shared.service;

import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
public interface CrudService<REQ, RES, Id> {
	@Transactional
	RES create(@Valid REQ request);
	@Transactional
	RES update(Id id, @Valid REQ request);
	@Transactional(readOnly = true)
	RES read(Id id);
	@Transactional
	void delete(Id id);
}