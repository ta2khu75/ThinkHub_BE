package com.ta2khu75.thinkhub.notification.required.port;

import com.ta2khu75.thinkhub.post.api.dto.PostResponse;

public interface NotificationPostPort {
	PostResponse read(Long id);
}
