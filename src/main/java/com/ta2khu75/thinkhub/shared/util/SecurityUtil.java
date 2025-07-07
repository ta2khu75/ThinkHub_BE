package com.ta2khu75.thinkhub.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.ta2khu75.thinkhub.account.entity.AccountProfile;
import com.ta2khu75.thinkhub.shared.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.UnAuthenticatedException;

public final class SecurityUtil {
	private SecurityUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static Jwt getJwtToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthenticationToken jwtAuth) {
			return jwtAuth.getToken();
		}
		throw new UnAuthenticatedException("You must be login");
	}

	private static <T> T getClaim(String claimName, Class<T> clazz) {
		Jwt jwt = getJwtToken();
		T claim = jwt.getClaim(claimName);
		if (claim == null) {
			throw new UnAuthenticatedException("Missing claim: " + claimName);
		}
		return claim;
	}

	public static String getCurrentAccountId() {
		return getJwtToken().getSubject();
	}

	public static Long getCurrentProfileId() {
		return getClaim("profileId", Long.class);
	}

	public static Long getCurrentStatusId() {
		return getClaim("statusId", Long.class);
	}

	public static AccountProfile getCurrentProfile() {
		AccountProfile profile = new AccountProfile();
		profile.setId(getClaim("profileId", Long.class));
		return profile;
	}

	private static String extractAuthorities(Authentication authentication) {
		return authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
	}

	public static String getCurrentRole() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return extractAuthorities(securityContext.getAuthentication());
	}

	public static boolean isAuthor(Long id) {
		try {
			Long accountId = getCurrentProfileId();
			return accountId.equals(id);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAdmin() {
		return RoleDefault.ADMIN.name().equals(getCurrentRole());
	}
}
