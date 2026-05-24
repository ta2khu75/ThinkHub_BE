package com.ta2khu75.thinkhub.modules.authz.internal.config;

import java.util.function.Supplier;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.google.api.gax.rpc.UnauthenticatedException;
import com.ta2khu75.thinkhub.modules.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.modules.authz.role.RoleApi;
import com.ta2khu75.thinkhub.shared.domain.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class EndpointAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	RedisService redisService;
	RoleApi roleService;

	private boolean isAdmin(String roleName) {
		return RoleDefault.ADMIN.name().equals(roleName);
	}

	private boolean isAllowedEndpoint(RoleSummary role, String requestPath, String httpMethod) {
		System.out.println(requestPath);
		System.out.println(httpMethod);

		AntPathMatcher pathMatcher = new AntPathMatcher();
		return role.permissions().stream().anyMatch(permission -> {
			boolean resultHppt = httpMethod.equals(permission.getMethod().name());
			boolean pathResult = pathMatcher.match(permission.getPattern(), requestPath);
			return resultHppt && pathResult;
		});
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		return new AuthorizationDecision(this.authorize(authentication, object).isGranted());
	}

	@Override
	public AuthorizationResult authorize(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		HttpServletRequest request = object.getRequest();
		String requestUrl = request.getRequestURI();
		String httpMethod = request.getMethod();
		System.out.println(requestUrl);
		System.out.println(httpMethod);
		try {
			String username = SecurityUtil.getCurrentUsername();
			System.out.println("username: " + username);
			Long userId = SecurityUtil.getCurrentUserIdDecode();
			boolean exists = redisService.exists(RedisKeyBuilder.userLock(userId));
			if (exists) {
				throw new AccessDeniedException("Account locked");
			}
		} catch (UnauthenticatedException e) {
			e.printStackTrace();
			System.out.println("da bat loi lay id tai khoan");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String roleName = SecurityUtil.getCurrentRole();
		if (isAdmin(roleName)) {
			return new AuthorizationDecision(true);
		}
		RoleSummary role = roleService.readSummaryByName(roleName);
		boolean isAllowed = isAllowedEndpoint(role, requestUrl, httpMethod);
		if (isAllowed) {
			return new AuthorizationDecision(true);
		}
		return new AuthorizationDecision(false);
	}
}
