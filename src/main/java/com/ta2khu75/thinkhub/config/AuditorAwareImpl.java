package com.ta2khu75.thinkhub.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

public class AuditorAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		try {
			return Optional.ofNullable(SecurityUtil.getCurrentUserIdDecode());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
