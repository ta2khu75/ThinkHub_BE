package com.ta2khu75.thinkhub.shared.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;
import com.ta2khu75.thinkhub.shared.service.ExistsService;
import com.ta2khu75.thinkhub.shared.service.clazz.ExistsServiceRegistry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SharedListener {
	ExistsServiceRegistry registry;

	@EventListener
	public void handleCheckExistsEvent(CheckExistsEvent<?> event) {
		ExistsService<Object> existsService = registry.getService(event.entityType());
		existsService.checkExists(event.id());
	}
}
