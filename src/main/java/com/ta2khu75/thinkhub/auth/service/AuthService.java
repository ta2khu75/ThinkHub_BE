package com.ta2khu75.thinkhub.auth.service;

import com.ta2khu75.thinkhub.auth.AuthResponse;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.LoginRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;

import jakarta.mail.MessagingException;

public interface AuthService {
	AuthResponse login(LoginRequest request);

	void register(RegisterRequest request) throws MessagingException;

	void changePassword(ChangePasswordRequest request);

	AuthResponse refreshToken(String token);

	void logout(String token);

}
