package com.ta2khu75.thinkhub.auth.api;

import com.ta2khu75.thinkhub.auth.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.auth.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.auth.api.dto.RegisterRequest;

import jakarta.mail.MessagingException;

public interface AuthApi {
	AuthResponse login(LoginRequest request);

	void register(RegisterRequest request) throws MessagingException;

	void changePassword(ChangePasswordRequest request);

	AuthResponse refreshToken(String token);

	void logout(String token);

}
