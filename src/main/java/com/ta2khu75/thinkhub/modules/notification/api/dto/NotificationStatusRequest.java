package com.ta2khu75.thinkhub.modules.notification.api.dto;

import com.ta2khu75.thinkhub.modules.notification.api.NotificationStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NotificationStatusRequest(@NotNull @Valid NotificationIdDto id, @NotNull NotificationStatus status) {

}
