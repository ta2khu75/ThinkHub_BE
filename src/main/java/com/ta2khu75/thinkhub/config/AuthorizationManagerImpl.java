package com.ta2khu75.thinkhub.config;

import java.util.function.Supplier;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.google.api.gax.rpc.UnauthenticatedException;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.dto.RoleDto;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
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
		System.out.println(requestPath);
		System.out.println(httpMethod);
		return role.permissions().stream().anyMatch(permission -> {
			boolean resultHppt = httpMethod.equals(permission.requestMethod().name());
			boolean pathResult = pathMatcher.match(permission.pattern(), requestPath);
			return resultHppt && pathResult;
		});
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest object) {
		// Tối thiểu, để tránh lỗi biên dịch
		// Có thể ném UnsupportedOperationException nếu bạn không muốn dùng nó
		throw new UnsupportedOperationException("Use authorize() instead");
	}

	@Override
	public AuthorizationResult authorize(Supplier<Authentication> authentication, HttpServletRequest object) {
		String requestUrl = object.getRequestURI();
		String httpMethod = object.getMethod();
		try {
			Long accountId = SecurityUtil.getCurrentAccountIdDecode();
			boolean exists = redisService.exists(RedisKeyBuilder.accountLock(accountId));
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
		RoleDto role = roleService.readDtoByName(roleName);
		boolean isAllowed = isAllowedEndpoint(role, requestUrl, httpMethod);
		if (true) {
			return new AuthorizationDecision(true);
		}
		return new AuthorizationDecision(false);
	}

}
