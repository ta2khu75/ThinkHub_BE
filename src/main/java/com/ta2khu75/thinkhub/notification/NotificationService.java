package com.ta2khu75.thinkhub.notification;

import com.ta2khu75.thinkhub.notification.dto.NotificationIdDto;
import com.ta2khu75.thinkhub.notification.dto.NotificationRequest;
import com.ta2khu75.thinkhub.notification.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.dto.NotificationStatusRequest;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.dto.Search;

public interface NotificationService {
	NotificationResponse create(NotificationRequest request);

	NotificationResponse update(NotificationStatusRequest request);

	void delete(NotificationIdDto id);

	PageResponse<NotificationResponse> readPage(Search pageable);
}
