package com.ta2khu75.thinkhub.account.listener;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.repository.AccountRepository;
import com.ta2khu75.thinkhub.auth.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountListener {
	private final AccountService service;
	@ApplicationModuleListener
	public void registerAccountListener(RegisterRequest request) {
		
	}
}
