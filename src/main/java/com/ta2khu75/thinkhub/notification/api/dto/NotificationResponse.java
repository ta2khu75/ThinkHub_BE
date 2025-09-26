package com.ta2khu75.thinkhub.notification.api.dto;


import com.ta2khu75.thinkhub.notification.api.NotificationStatus;
import com.ta2khu75.thinkhub.shared.api.dto.BaseClassResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse extends BaseClassResponse<NotificationIdDto> {
	NotificationStatus status;
	Object target;
}
