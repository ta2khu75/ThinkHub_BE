package com.ta2khu75.thinkhub.modules.notification.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.modules.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.modules.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.modules.notification.internal.domain.Notification;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface NotificationMapper
		extends Converter<Notification, NotificationResponse>, PageMapper<Notification, NotificationResponse> {
	@Override
	@Mapping(target = "target", ignore = true)
	@Mapping(target = "status", ignore = true)
	NotificationResponse convert(Notification entity);

	Notification toEntity(NotificationRequest request);
}
