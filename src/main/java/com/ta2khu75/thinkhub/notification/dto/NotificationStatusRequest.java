package com.ta2khu75.thinkhub.notification.dto;

import com.ta2khu75.thinkhub.notification.NotificationStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NotificationStatusRequest(@NotNull @Valid NotificationIdDto id, @NotNull NotificationStatus status) {

}
