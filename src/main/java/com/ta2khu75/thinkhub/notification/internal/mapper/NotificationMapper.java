package com.ta2khu75.thinkhub.notification.internal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.internal.entity.Notification;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface NotificationMapper
		extends Converter<Notification, NotificationResponse>, PageMapper<Notification, NotificationResponse> {
	@Override
	@Mapping(target = "target", ignore = true)
	@Mapping(target = "id", ignore = true)
	NotificationResponse convert(Notification entity);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", source = "request")
	Notification toEntity(NotificationRequest request);
}
