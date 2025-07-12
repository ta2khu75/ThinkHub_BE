package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;


@Mapper(config = MapperSpringConfig.class)
public interface IdMapper extends Converter<BaseEntityLong, String> {

	@Override
	@Named("encodeId")
	default String convert(BaseEntityLong entity) {
		if (entity instanceof IdConfigProvider configProvider) {
			return IdConverterUtil.encode(entity.getId(), configProvider.getIdConfig());
		}
		throw new IllegalArgumentException(
				"Entity is not an instance of SaltedIdentifiable: " + entity.getClass().getName());
	}

//	@Named("encode")
//	public String encode(Long id, @Context IdTypeProvider typeProvider) {
//		return idConverter.encode(id, typeProvider.getIdType());
//	}
//
//	@Named("decode")
//	public Long decode(String value, @Context IdTypeProvider typeProvider) {
//		return idConverter.decode(value, typeProvider.getIdType());
//	}
}
