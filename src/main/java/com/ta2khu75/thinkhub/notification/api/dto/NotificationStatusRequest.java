package com.ta2khu75.thinkhub.notification.api.dto;

import com.ta2khu75.thinkhub.notification.api.NotificationStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NotificationStatusRequest(@NotNull @Valid NotificationIdDto id, @NotNull NotificationStatus status) {

}
