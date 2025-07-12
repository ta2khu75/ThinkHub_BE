package com.ta2khu75.thinkhub.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.ta2khu75.thinkhub.shared.util.SecurityUtil;


public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			return Optional.ofNullable(SecurityUtil.getCurrentUsername());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
