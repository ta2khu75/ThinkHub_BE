package com.ta2khu75.thinkhub.notification.api;

import com.ta2khu75.thinkhub.notification.api.dto.NotificationIdDto;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationStatusRequest;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

public interface NotificationApi {
	NotificationResponse create(NotificationRequest request);

	NotificationResponse update(NotificationStatusRequest request);

	void delete(NotificationIdDto id);

	PageResponse<NotificationResponse> readPage(Search pageable);
}
