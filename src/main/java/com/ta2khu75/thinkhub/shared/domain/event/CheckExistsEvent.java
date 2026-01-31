package com.ta2khu75.thinkhub.shared.domain.event;

import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;

public record CheckExistsEvent<Id>(EntityType entityType, Id id) {
}
