package com.ta2khu75.thinkhub.shared.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.config.IdProperties.IdType;
import com.ta2khu75.thinkhub.shared.service.clazz.IdConverterService;

public abstract class BaseConvertService<T, ID, R extends JpaRepository<T, ID>, M> extends BaseService<T, ID, R, M> {

	public BaseConvertService(R repository, M mapper, IdConverterService converter) {
		super(repository, mapper);
		this.converter = converter;
	}

	protected final IdConverterService converter;

	public Long decodeAccountId(String accountId) {
		return converter.decode(accountId, IdType.ACCOUNT);
	}

	public Long decode(String id, IdType idType) {
		return converter.decode(id, idType);
	}

}
