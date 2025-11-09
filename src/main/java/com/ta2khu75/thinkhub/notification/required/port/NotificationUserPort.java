package com.ta2khu75.thinkhub.notification.required.port;

import java.util.List;

public interface NotificationUserPort {
	List<Long> readAllUserIdByRoleId(Long id);
}
