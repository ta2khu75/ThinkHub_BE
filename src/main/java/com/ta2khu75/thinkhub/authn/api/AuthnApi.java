package com.ta2khu75.thinkhub.authn.api;

import com.ta2khu75.thinkhub.authn.api.dto.AuthSummary;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;

public interface AuthnApi {
	AuthSummary login(LoginRequest request);

	void create(UserCreateRequest request, String password);

	void createGeneratorPassword(UserCreateRequest request);

	void register(RegisterRequest request);

	void changePassword(ChangePasswordRequest request);

	AuthSummary refreshToken(String token);

	void logout(String token);
}
