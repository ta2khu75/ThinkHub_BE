package com.ta2khu75.thinkhub.config;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.request.AccountProfileRequest;
import com.ta2khu75.thinkhub.account.request.AccountRequest;
import com.ta2khu75.thinkhub.account.request.AccountStatusRequest;
import com.ta2khu75.thinkhub.authority.PermissionGroupService;
import com.ta2khu75.thinkhub.authority.PermissionService;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {
	AccountService accountService;
	RoleService roleService;
	PermissionService permissionService;
	PermissionGroupService permissionGroupService;
	ApplicationContext applicationContext;
	PublicEndpointRegistry publicEndpointRegistry;

	private Long initRole() {
		List<RoleResponse> roles = Arrays.stream(RoleDefault.values())
				.map(role -> roleService.create(new RoleRequest(role.name(), Set.of()))).toList();
		return roles.stream().filter(role -> role.name().equals(RoleDefault.ADMIN.name())).findFirst()
				.orElseThrow(() -> new NotFoundException("Not found role name ADMIN")).id();
	}

	private Set<Long> initPermission() {
		Set<Long> permissionPublic = new HashSet<>();
		Map<RequestMethod, Set<String>> publicEndpoints = Map.of(RequestMethod.GET,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.GET), RequestMethod.POST,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.POST), RequestMethod.PUT,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PUT), RequestMethod.PATCH,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PATCH), RequestMethod.DELETE,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.DELETE));

		Map<PermissionGroupRequest, Set<Long>> permissionGroupMap = new HashMap<>();
		Map<String, RequestMappingHandlerMapping> handlerMappingMap = applicationContext
				.getBeansOfType(RequestMappingHandlerMapping.class);
		for (RequestMappingHandlerMapping handlerMapping : handlerMappingMap.values()) {
			Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				RequestMappingInfo mappingInfo = entry.getKey();
				HandlerMethod handlerMethod = entry.getValue();
				Method method = handlerMethod.getMethod();
				Class<?> controllerClass = handlerMethod.getBeanType();
				if (!isValidHandler(controllerClass, method, mappingInfo))
					continue;
				Tag tag = controllerClass.getAnnotation(Tag.class);
				Operation operation = method.getAnnotation(Operation.class);
				PermissionGroupRequest permissionGroupRequest = new PermissionGroupRequest(tag.name(),
						tag.description(), Set.of());

				RequestMethod requestMethod = mappingInfo.getMethodsCondition().getMethods().iterator().next();
				String pattern = mappingInfo.getPathPatternsCondition().getPatternValues().iterator().next();
				PermissionRequest permissionRequest = new PermissionRequest(operation.summary(),
						operation.description(), pattern, requestMethod);
//				createOrUpdatePermission(permissionRequest);
				PermissionResponse permission = createOrUpdatePermission(permissionRequest);
				permissionGroupMap.computeIfAbsent(permissionGroupRequest, k -> new HashSet<>()).add(permission.id());
				if (publicEndpoints.getOrDefault(requestMethod, Set.of()).contains(pattern)) {
					permissionPublic.add(permission.id());
				}

			}
		}
		for (Map.Entry<PermissionGroupRequest, Set<Long>> entry : permissionGroupMap.entrySet()) {
			PermissionGroupRequest request = entry.getKey();
			PermissionGroupRequest permissionGroupRequest = new PermissionGroupRequest(request.name(),
					request.description(), entry.getValue());
			Optional<PermissionGroupResponse> optional = permissionGroupService.findByName(entry.getKey().name());
			if (optional.isPresent()) {
				Integer id = optional.get().id();
				permissionGroupService.update(id, permissionGroupRequest);
			} else {
				permissionGroupService.create(permissionGroupRequest);
			}
		}
		return permissionPublic;
	}

	private boolean isValidHandler(Class<?> controllerClass, Method method, RequestMappingInfo mappingInfo) {
		return controllerClass.isAnnotationPresent(Tag.class) && method.isAnnotationPresent(Operation.class)
				&& !mappingInfo.getMethodsCondition().getMethods().isEmpty()
				&& !mappingInfo.getPathPatternsCondition().getPatternValues().isEmpty();
	}

	private PermissionResponse createOrUpdatePermission(PermissionRequest request) {
		return permissionService.findBySummary(request.summary()).map(p -> permissionService.update(p.id(), request))
				.orElseGet(() -> permissionService.create(request));
	}

	@Transactional
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (accountService.count() == 0) {
			AccountStatusRequest status = new AccountStatusRequest(true, true, initRole());
			AccountProfileRequest profile = new AccountProfileRequest("admin", "admin", LocalDate.now(), "ta2khu75");
			AccountRequest account = new AccountRequest("admin", "123456", "123456", "admin@g.com", profile, status);
			accountService.create(account);
		}
		Set<Long> permissionSet = initPermission();
		RoleResponse role = roleService.readByName(RoleDefault.ANONYMOUS.name());
		if (role.permissionIds() == null) {
			roleService.update(role.id(), new RoleRequest(role.name(), permissionSet));
		} else {
			roleService.update(role.id(), new RoleRequest(role.name(),
					Stream.concat(role.permissionIds().stream(), permissionSet.stream()).collect(Collectors.toSet())));
		}

	}

}
