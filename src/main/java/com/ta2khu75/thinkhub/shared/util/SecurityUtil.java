package com.ta2khu75.thinkhub.shared.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.UnauthorizedException;

public final class SecurityUtil {
	private SecurityUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private static Jwt getJwtToken() {
		if (getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
			return jwtAuth.getToken();
		}
		throw new UnauthorizedException("You must be login");
	}

	private static <T> T getClaim(String claimName, Class<T> clazz) {
		Jwt jwt = getJwtToken();
		T claim = jwt.getClaim(claimName);
		if (claim == null) {
			throw new UnauthorizedException("Missing claim: " + claimName);
		}
		return claim;
	}

	private static String extractAuthorities(Authentication authentication) {
		return authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
	}

	public static String getCurrentUsername() {
		return getClaim("username", String.class);
	}

	public static String getCurrentUserId() {
		return getJwtToken().getSubject();
	}

	public static Long getCurrentUserIdDecode() {
		return IdConverterUtil.decode(getCurrentUserId(), IdConfig.USER);
	}

	public static String getCurrentRole() {
		return extractAuthorities(getAuthentication());
	}

	public static boolean isAuthor(String id) {
		try {
			String accountId = getCurrentUserId();
			return accountId.equals(id);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAuthorDecode(Long id) {
		try {
			Long accountId = getCurrentUserIdDecode();
			return accountId.equals(id);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isAdmin() {
		return RoleDefault.ADMIN.name().equals(getCurrentRole());
	}
}
