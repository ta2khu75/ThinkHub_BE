package com.ta2khu75.thinkhub.authn.api;

import com.ta2khu75.thinkhub.authn.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserSummary;

public interface AuthnApi {
	AuthResponse login(LoginRequest request);

	void register(RegisterRequest request);

	void create(UserSummary user);

	void create(UserSummary user, String password);

	void changePassword(ChangePasswordRequest request);

	AuthResponse refreshToken(String token);

	void logout(String token);

}
