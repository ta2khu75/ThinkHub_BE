package com.ta2khu75.thinkhub.shared.event;

import com.ta2khu75.thinkhub.shared.enums.EntityType;

public record CheckExistsEvent<Id>(EntityType entityType, Id id) {
}
