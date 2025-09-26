package com.ta2khu75.thinkhub.authn.internal.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ta2khu75.thinkhub.authn.internal.model.GoogleUser;

@Service
public class GoogleAuthService {
	private static final String CLIENT_ID = "719913834451-c01vuuhgtmlk39q5o9cb615kqb868nc1.apps.googleusercontent.com";
	private final GoogleIdTokenVerifier verifier;

	public GoogleAuthService() {
		this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
				.setAudience(Collections.singletonList(CLIENT_ID)).build();
	}

	public GoogleUser verifyToken(String idTokenString) throws GeneralSecurityException, IOException {
		GoogleIdToken idToken = verifier.verify(idTokenString);
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			// Lấy thông tin user từ payload
			String userId = payload.getSubject(); // unique ID Google
			String email = payload.getEmail();
			boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			return new GoogleUser(userId, email, emailVerified, name, pictureUrl);
		} else {
			throw new IllegalArgumentException("Invalid ID token.");
		}
	}
}
