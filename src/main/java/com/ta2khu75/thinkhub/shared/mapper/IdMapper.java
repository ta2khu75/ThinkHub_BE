package com.ta2khu75.thinkhub.shared.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.entity.IdEntity;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;

@Mapper(config = MapperSpringConfig.class)
public interface IdMapper extends Converter<IdEntity<Long>, String> {

	@Override
	default String convert(IdEntity<Long> entity) {
		if (entity instanceof IdConfigProvider configProvider) {
			return IdConverterUtil.encode(entity.getId(), configProvider.getIdConfig());
		}
		throw new IllegalArgumentException(
				"Entity is not an instance of SaltedIdentifiable: " + entity.getClass().getName());
	}
//	@Named("decodeId")
//	default String encodeId(BaseResponse<Long> entity) {
//		if (entity instanceof SaltedIdentifiable salted) {
//			return SqidsUtil.encodeWithSalt(entity.getId(), salted.getSaltedType());
//		}
//		throw new IllegalArgumentException("Entity is not an instance of SaltedIdentifiable: " + entity.getClass().getName());
//	}
//	
//	
//
//	@Named("encode")
//	default String encode(Long id, @Context IdConfig idConfig) {
//		return IdConverterUtil.encode(id, idConfig);
//	}

//	@Named("decode")
//	default Long decode(String value, @Context SaltedIdentifiable salted) {
//		return SqidsUtil.decodeWithSalt(value, salted.getSaltedType());
//	}
}
