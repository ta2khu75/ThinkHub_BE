package com.ta2khu75.thinkhub.config;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.authority.PermissionGroupService;
import com.ta2khu75.thinkhub.authority.PermissionService;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {
	AccountService accountService;
	RoleService roleService;
	PermissionService permissionService;
	PermissionGroupService permissionGroupService;
	PasswordEncoder passwordEncoder;
	ApplicationContext applicationContext;
	PublicEndpointRegistry publicEndpointRegistry;

	private Long initRole() {
		List<RoleResponse> roles = Arrays.stream(RoleDefault.values())
				.map(role -> roleService.create(new RoleRequest(role.name(), Set.of()))).toList();
		return roles.stream().filter(role -> role.name().equals(RoleDefault.ADMIN.name())).findFirst()
				.orElseThrow(() -> new NotFoundException("Not found role name ADMIN")).id();
	}

	private Set<PermissionResponse> initPermission() {
		Set<PermissionResponse> permissionPublic = new HashSet<>();
		Set<String> publicPostEndpoint = publicEndpointRegistry.getPublicEndpoint(RequestMethod.POST);
		Set<String> publicGetEndpoint = publicEndpointRegistry.getPublicEndpoint(RequestMethod.GET);
		Map<String, RequestMappingHandlerMapping> requestMappingMap = applicationContext
				.getBeansOfType(RequestMappingHandlerMapping.class);
		for (RequestMappingHandlerMapping handlerMapping : requestMappingMap.values()) {
			handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
				Method method = handlerMethod.getMethod();
				if (method.isAnnotationPresent(Opera.class)) {
					EndpointMapping endpointMapping = method.getAnnotation(EndpointMapping.class);
					RequestMethod httpMethod = requestMappingInfo.getMethodsCondition().getMethods().iterator().next();
					String path = requestMappingInfo.getPathPatternsCondition().getPatternValues().iterator().next();
					String permissionGroupName = StringUtil
							.convertCamelCaseToReadable(method.getDeclaringClass().getSimpleName());
					PermissionGroup permissionGroup = permissionGroupRepository.findByName(permissionGroupName);
					if (permissionGroup == null) {
						permissionGroup = permissionGroupRepository
								.save(PermissionGroup.builder().name(permissionGroupName).build());
					}
					Permission permission = permissionRepository.findByName(endpointMapping.name());
					if (permission != null) {
						permission.setPath(path);
						permission.setHttpMethod(httpMethod);
						permission.setDescription(endpointMapping.description());
						permission.setPermissionGroup(permissionGroup);
					} else {
						permission = Permission.builder().name(endpointMapping.name())
								.description(endpointMapping.description()).path(path).httpMethod(httpMethod)
								.permissionGroup(permissionGroup).build();
					}
					permission = permissionRepository.save(permission);
					switch (httpMethod) {
					case GET: {
						endpointUtil.getPublicEndpoint(httpMethod);
						if (publicGetEndpoint.contains(path))
							permissions.add(permission);
						break;
					}
					case POST: {
						if (publicPostEndpoint.contains(path))
							permissions.add(permission);
						break;
					}
					case PUT: {
						break;
					}
					case PATCH: {
						break;
					}
					case DELETE: {
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + httpMethod);
					}
				}
			});
		}
		return permissions;
	}

	@Transactional
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (accountService.count() == 0) {
			AccountStatusRequest status = new AccountStatusRequest(true, true, initRole());
			AccountProfileRequest profile = new AccountProfileRequest("admin", "admin", Instant.now(), "ta2khu75");
			AccountRequest account = new AccountRequest("admin@g.com", passwordEncoder.encode("123456"), profile, status)
					Account.builder().email("admin@g.com").password(passwordEncoder.encode("123"))
					.status(status).profile(profile).build();
			accountRepository.save(account);
		}
		Set<Permission> permissionSet = initPermission();
		Role role = roleRepository.findByName(RoleDefault.ANONYMOUS.name())
				.orElseThrow(() -> new NotFoundException("Could not find role name " + RoleDefault.ANONYMOUS.name()));
		if (role.getPermissions().isEmpty()) {
			role.setPermissions(permissionSet);
		}

	}

}
