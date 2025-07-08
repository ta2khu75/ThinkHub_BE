package com.ta2khu75.thinkhub.account.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.auth.ChangePasswordRequest;
import com.ta2khu75.thinkhub.auth.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountListener {
	private final AccountService service;

	@EventListener
	void registerAccountListener(RegisterRequest request) {
		service.register(request);
	}

	@EventListener
	void changePassword(ChangePasswordRequest request) {
		service.changePassword(request);
	}
}
