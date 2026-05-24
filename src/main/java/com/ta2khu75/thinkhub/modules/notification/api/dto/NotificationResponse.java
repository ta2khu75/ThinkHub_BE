package com.ta2khu75.thinkhub.modules.notification.api.dto;

import com.ta2khu75.thinkhub.modules.notification.api.model.NotificationStatus;
import com.ta2khu75.thinkhub.shared.common.api.dto.BaseClassResponse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse extends BaseClassResponse<Long> {
	NotificationStatus status;
	Object target;
}
