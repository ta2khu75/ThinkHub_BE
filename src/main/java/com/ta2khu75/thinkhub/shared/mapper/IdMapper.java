package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfigRegistry;
import com.ta2khu75.thinkhub.shared.domain.entity.HasIdSubject;
import com.ta2khu75.thinkhub.shared.domain.entity.IdEntity;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;

@Mapper(config = MapperSpringConfig.class)
public interface IdMapper extends Converter<IdEntity<Long>, String> {

	@Override
	default String convert(IdEntity<Long> entity) {
		if (entity instanceof HasIdSubject configProvider) {
			IdConfig config = IdConfigRegistry.resolve(configProvider.getIdSubject());
			return IdConverterUtil.encode(entity.getId(), config);
		}
		throw new IllegalArgumentException(
				"Entity is not an instance of SaltedIdentifiable: " + entity.getClass().getName());
	}
}
