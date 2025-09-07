package com.ta2khu75.thinkhub.authentication.api;

import com.ta2khu75.thinkhub.authentication.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authentication.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.CreateAuthProviderRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authentication.api.dto.RegisterRequest;

public interface AuthenticationApi {
	AuthResponse login(LoginRequest request);

	void register(RegisterRequest request);

	void create(CreateAuthProviderRequest request);

	void changePassword(ChangePasswordRequest request);

	AuthResponse refreshToken(String token);

	void logout(String token);
	
	long count();
}
