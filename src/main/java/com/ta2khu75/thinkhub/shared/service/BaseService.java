package com.ta2khu75.thinkhub.shared.service;

import java.lang.reflect.ParameterizedType;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.shared.exception.NotFoundException;

public abstract class BaseService<T, ID, R extends JpaRepository<T, ID>> {
	@SuppressWarnings("unchecked")
	protected BaseService(R repository) {
		super();
		this.repository = repository;
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private final Class<T> clazz;
	protected final R repository;

	public T readEntity(ID id) {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(clazz.getSimpleName() + ":NOT_FOUND",
				"Could not find " + clazz.getSimpleName() + " with id " + id));
	}

	public void assertExists(ID id) {
		if (!repository.existsById(id)) {
			new NotFoundException(clazz.getSimpleName() + "_NOT_FOUND",
					"Could not find " + clazz.getSimpleName() + " with id " + id);
		}
	}
}
