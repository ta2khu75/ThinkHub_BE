package com.ta2khu75.thinkhub.authorization.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ta2khu75.thinkhub.authorization.api.AuthorizationApi;
import com.ta2khu75.thinkhub.authorization.api.dto.request.PermissionGroupRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.request.PermissionRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authorization.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.authorization.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.authorization.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.authorization.internal.group.PermissionGroupService;
import com.ta2khu75.thinkhub.authorization.internal.permission.PermissionService;
import com.ta2khu75.thinkhub.authorization.internal.role.RoleService;
import com.ta2khu75.thinkhub.config.PublicEndpointRegistry;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizationServiceImpl implements AuthorizationApi {
	PermissionGroupService groupService;
	PermissionService permissionService;
	RoleService roleService;
	ApplicationContext applicationContext;
	PublicEndpointRegistry publicEndpointRegistry;

	@Override
	public List<PermissionGroupResponse> readAllGroups() {
		return groupService.readAll();
	}

	@Override
	public List<RoleResponse> readAllRoles() {
		return roleService.readAll();
	}

	@Override
	public RoleResponse readRole(Long id) {
		return roleService.read(id);
	}

	@Override
	public RoleResponse readRoleByName(String name) {
		return roleService.readByName(name);
	}

	@Override
	public RoleResponse createRole(RoleRequest request) {
		return roleService.create(request);
	}

	@Override
	public RoleResponse updateRole(Long id, RoleRequest request) {
		return roleService.update(id, request);
	}

	@Override
	public void deleteRole(Long id) {
		roleService.delete(id);
	}

	@Override
	public Long initDefaultRoles() {
		List<RoleResponse> roles = Arrays.stream(RoleDefault.values())
				.map(role -> roleService.create(new RoleRequest(role.name(), null, Set.of()))).toList();
		return roles.stream().filter(role -> role.name().equals(RoleDefault.ADMIN.name())).findFirst()
				.orElseThrow(() -> new NotFoundException("Not found role name ADMIN")).id();
	}

	@Override
	public Set<Long> initPermissionsFromEndpoints() {
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
			Optional<PermissionGroupResponse> optional = groupService.findByName(entry.getKey().name());
			if (optional.isPresent()) {
				Integer id = optional.get().id();
				groupService.update(id, permissionGroupRequest);
			} else {
				groupService.create(permissionGroupRequest);
			}
		}
		return permissionPublic;
	}

	@Override
	public void assignPermissionsToRole(String roleName, Set<Long> permissionIds) {
		RoleResponse role = roleService.readByName(RoleDefault.ANONYMOUS.name());
		if (role.permissionIds() == null) {
			roleService.update(role.id(), new RoleRequest(role.name(), role.description(), permissionIds));
		} else {
			roleService.update(role.id(), new RoleRequest(role.name(), role.description(),
					Stream.concat(role.permissionIds().stream(), permissionIds.stream()).collect(Collectors.toSet())));
		}

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
}
