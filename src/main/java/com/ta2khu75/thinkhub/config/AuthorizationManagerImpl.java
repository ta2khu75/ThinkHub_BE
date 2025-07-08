package com.ta2khu75.thinkhub.config;

import java.util.function.Supplier;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.ta2khu75.thinkhub.authority.RoleDto;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.shared.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.UnAuthenticatedException;
import com.ta2khu75.thinkhub.shared.service.RedisService;
import com.ta2khu75.thinkhub.shared.service.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthorizationManagerImpl implements AuthorizationManager<HttpServletRequest> {
	@NonFinal
	AntPathMatcher pathMatcher = new AntPathMatcher();
	RoleService roleService;
	RedisService redisService;

	private boolean isAdmin(String roleName) {
		return RoleDefault.ADMIN.name().equals(roleName);
	}

	private boolean isAllowedEndpoint(RoleDto role, String requestPath, String httpMethod) {

		return role.permissions().stream().anyMatch(permission -> {
			boolean resultHppt = httpMethod.equals(permission.requestMethod().name());
			boolean pathResult = pathMatcher.match(permission.pattern(), requestPath);
			return resultHppt && pathResult;
		});
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest object) {
		String requestUrl = object.getRequestURI();
		String httpMethod = object.getMethod();
		try {
			String accountId = SecurityUtil.getCurrentAccountId();
			boolean exists = redisService.exists(RedisKeyBuilder.accountLock(accountId));
			if (exists) {
				throw new AccessDeniedException("Account locked");
			}
		} catch (UnAuthenticatedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String roleName = SecurityUtil.getCurrentRole();
		if (isAdmin(roleName)) {
			return new AuthorizationDecision(true);
		}
		RoleDto role = roleService.readDtoByName(roleName);
		boolean isAllowed = isAllowedEndpoint(role, requestUrl, httpMethod);
		if (isAllowed) {
			return new AuthorizationDecision(true);
		}
		throw new AccessDeniedException("Access denied");
	}
}
