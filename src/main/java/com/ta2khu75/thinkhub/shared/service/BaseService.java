package com.ta2khu75.thinkhub.shared.service;

import java.lang.reflect.ParameterizedType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.shared.exception.NotFoundException;

public abstract class BaseService<T, ID, R extends JpaRepository<T, ID>, M> {
	@SuppressWarnings("unchecked")
	protected BaseService(R repository, M mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
		this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private final Class<T> clazz;
	protected final R repository;
	protected final M mapper;
	@Transactional(readOnly = true)
	public T readEntity(ID id) {
		return repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Could not find " + clazz.getSimpleName() + " with id " + id));
	}

}
