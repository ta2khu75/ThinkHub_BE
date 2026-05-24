package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfigRegistry;
import com.ta2khu75.thinkhub.shared.domain.entity.HasPublicId;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;

@Mapper(config = MapperSpringConfig.class)
public interface IdMapper extends Converter<HasPublicId, String> {

	@Override
	default String convert(HasPublicId hasPublicId) {
		IdConfig config = IdConfigRegistry.resolve(hasPublicId.getIdSubject());
		return IdConverterUtil.encode(hasPublicId.getId(), config);
	}
}
