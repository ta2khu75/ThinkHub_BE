package com.ta2khu75.thinkhub.user.required.port;

import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

public interface UserAuthnPort {
	void create(UserSummary user);
}
