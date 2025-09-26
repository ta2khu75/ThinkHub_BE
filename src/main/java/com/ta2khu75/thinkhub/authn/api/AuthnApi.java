package com.ta2khu75.thinkhub.authn.api;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.ta2khu75.thinkhub.authn.api.dto.AuthResponse;
import com.ta2khu75.thinkhub.authn.api.dto.ChangePasswordRequest;
import com.ta2khu75.thinkhub.authn.api.dto.CreateAuthProviderRequest;
import com.ta2khu75.thinkhub.authn.api.dto.GoogleRequest;
import com.ta2khu75.thinkhub.authn.api.dto.LoginRequest;
import com.ta2khu75.thinkhub.authn.api.dto.RegisterRequest;

public interface AuthnApi {
	AuthResponse login(LoginRequest request);

	void register(RegisterRequest request);

	void create(CreateAuthProviderRequest request);

	void changePassword(ChangePasswordRequest request);

	AuthResponse refreshToken(String token);

	void logout(String token);
	
	long count();
	
	AuthResponse authenticationWithGoogle(GoogleRequest googleRequest) throws GeneralSecurityException, IOException;
}
