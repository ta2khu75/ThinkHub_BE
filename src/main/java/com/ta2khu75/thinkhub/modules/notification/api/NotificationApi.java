package com.ta2khu75.thinkhub.modules.notification.api;

import com.ta2khu75.thinkhub.modules.notification.api.dto.NotificationRequest;
import com.ta2khu75.thinkhub.modules.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

public interface NotificationApi {
	NotificationResponse create(NotificationRequest request);

	NotificationResponse read(Long id);

	NotificationResponse watch(Long id);
//	NotificationResponse update(NotificationStatusRequest request);

	void delete(Long id);

	PageResponse<NotificationResponse> readPage(Search pageable);
}
