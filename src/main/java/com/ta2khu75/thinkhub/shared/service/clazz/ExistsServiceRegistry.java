package com.ta2khu75.thinkhub.shared.service.clazz;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

@Component
public class ExistsServiceRegistry {
	private final Map<EntityType, ExistsService<?>> registry = new EnumMap<>(EntityType.class);

	public ExistsServiceRegistry(List<ExistsService<?>> services) {
		for (ExistsService<?> service : services) {
			registry.put(service.getEntityType(), service);
		}
	}

	@SuppressWarnings("unchecked")
	public ExistsService<Object> getService(EntityType entityType) {
		return (ExistsService<Object>) registry.get(entityType);
	}
}
