package com.ta2khu75.thinkhub.modules.notification.required.port;

import com.ta2khu75.thinkhub.modules.post.api.dto.PostResponse;

public interface NotificationPostPort {
	PostResponse read(Long id);
}
