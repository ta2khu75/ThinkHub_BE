package com.ta2khu75.thinkhub.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.ta2khu75.thinkhub.shared.util.SecurityUtil;


public class ApplicationAuditAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			return Optional.ofNullable(SecurityUtil.getCurrentAccountId());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
