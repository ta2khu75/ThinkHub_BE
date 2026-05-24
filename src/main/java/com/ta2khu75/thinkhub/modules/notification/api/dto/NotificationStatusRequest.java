package com.ta2khu75.thinkhub.modules.notification.api.dto;

import com.ta2khu75.thinkhub.modules.notification.api.model.NotificationStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NotificationStatusRequest(@NotNull @Valid Long id, @NotNull NotificationStatus status) {

}
