package com.ta2khu75.thinkhub.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ta2khu75.quiz.model.entity.Notification;
import com.ta2khu75.quiz.model.response.NotificationResponse;

@Mapper(componentModel = "spring", uses = { AccountMapper.class })
public interface NotificationMapper
		extends PageMapper<Notification, NotificationResponse> {
	@Mapping(target = "target", ignore = true)
//	@Mapping(target = "info", source = "entity")
	NotificationResponse toResponse(Notification entity);
}
