//package com.ta2khu75.thinkhub.authz.internal;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupRequest;
//import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionRequest;
//import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;
//import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionResponse;
//import com.ta2khu75.thinkhub.authz.internal.group.PermissionGroupService;
//import com.ta2khu75.thinkhub.authz.internal.permission.PermissionService;
//import com.ta2khu75.thinkhub.config.PublicEndpointRegistry;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.Operation;
//import io.swagger.v3.oas.models.PathItem;
//import io.swagger.v3.oas.models.tags.Tag;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@Lazy
//@RequiredArgsConstructor
//public class SwaggerService {
//	private final OpenAPI openAPI;
//	private final PermissionService permissionService;
//	private final PublicEndpointRegistry publicEndpointRegistry;
//	private final PermissionGroupService groupService;
//
//	private String getTag(Operation operation) {
//		List<String> tags = operation.getTags();
//		if (tags == null || tags.isEmpty())
//			return "PERMISSION_NO_TAG";
//		return getTagName(tags.getFirst());
//	}
//
//	private String getTagName(String tagName) {
//		return tagName.replace(" ", "_").toUpperCase();
//	}
//
//	public Set<Long> getPermissionPublicIds(Map<String, Set<PermissionRequest>> permissionMap) {
//		Map<RequestMethod, Set<String>> publicEndpoints = Map.of(RequestMethod.GET,
//				publicEndpointRegistry.getPublicEndpoint(RequestMethod.GET), RequestMethod.POST,
//				publicEndpointRegistry.getPublicEndpoint(RequestMethod.POST), RequestMethod.PUT,
//				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PUT), RequestMethod.PATCH,
//				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PATCH), RequestMethod.DELETE,
//				publicEndpointRegistry.getPublicEndpoint(RequestMethod.DELETE));
//		Set<String> publicCode = new HashSet<>();
//		permissionMap.values().forEach(permissionSet -> {
//			permissionSet.forEach(permission -> {
//				if (publicEndpoints.getOrDefault(permission.method(), Set.of()).contains(permission.pattern())) {
//					publicCode.add(permission.code());
//				}
//			});
//		});
//		return permissionService.readAllByCode(publicCode).stream().map(PermissionResponse::id)
//				.collect(Collectors.toSet());
//	}
//
//	public void getPermissionGroups(Map<String, Set<PermissionResponse>> permissionMap) {
//		List<PermissionGroupRequest> result = new ArrayList<>();
//		openAPI.getTags().forEach(tag -> {
//			String tagName = getTagName(tag.getName());
//			Set<PermissionResponse> permissions = permissionMap.get(tagName);
//			PermissionGroupRequest permissionGroupRequest = mapPermissionGroup(tagName, tag, permissions);
//			result.add(permissionGroupRequest);
//		});
//		groupService.saveAll(result);
//	}
//
//	private PermissionGroupRequest mapPermissionGroup(String tagName, Tag tag, Set<PermissionResponse> permissions) {
//		try {
//			PermissionGroupResponse group = groupService.readByName(tagName);
//			return new PermissionGroupRequest(group.id(), tag.getName(), tag.getDescription(),
//					permissions.stream().map(PermissionResponse::id).collect(Collectors.toSet()));
//		} catch (Exception e) {
//			return new PermissionGroupRequest(null, tag.getName(), tag.getDescription(),
//					permissions.stream().map(PermissionResponse::id).collect(Collectors.toSet()));
//		}
//	}
//
//	private Map<RequestMethod, Operation> getOperationMap(PathItem pathItem) {
//		Map<RequestMethod, Operation> result = new HashMap<>();
//		if (pathItem.getGet() != null) {
//			result.put(RequestMethod.GET, pathItem.getGet());
//		}
//		if (pathItem.getPost() != null) {
//			result.put(RequestMethod.POST, pathItem.getPost());
//		}
//		if (pathItem.getPut() != null) {
//			result.put(RequestMethod.PUT, pathItem.getPut());
//		}
//		if (pathItem.getDelete() != null) {
//			result.put(RequestMethod.DELETE, pathItem.getDelete());
//		}
//		if (pathItem.getPatch() != null) {
//			result.put(RequestMethod.PATCH, pathItem.getPatch());
//		}
//		return result;
//	}
//
//	public Map<String, Set<PermissionRequest>> getPermissionRequestMap() {
//		Map<String, Set<PermissionRequest>> result = new HashMap<>();
//		openAPI.getPaths().forEach((pattern, pathItem) -> {
//			Map<RequestMethod, Operation> operationMap = getOperationMap(pathItem);
//			operationMap.forEach((method, operation) -> {
//				result.computeIfAbsent(getTag(operation), k -> new HashSet<>())
//						.add(mapPermission(operation, method, pattern));
//			});
//		});
//		return result;
//	}
//
//	public Map<String, Set<PermissionResponse>> saveAllAndGetPermissionResponseMap(
//			Map<String, Set<PermissionRequest>> permissionRequestMap) {
//		Map<String, Set<PermissionResponse>> permissionResponseMap = new HashMap<>();
//		permissionRequestMap.forEach((tag, permissions) -> {
//			permissionResponseMap.put(tag, permissionService.saveAll(permissions).stream().collect(Collectors.toSet()));
//		});
//		return permissionResponseMap;
//	}
//
//	private PermissionRequest mapPermission(Operation operation, RequestMethod method, String pattern) {
//		try {
//			PermissionResponse permission = permissionService.readByCode(operation.getOperationId());
//			return new PermissionRequest(permission.id(), operation.getOperationId(), operation.getSummary(),
//					operation.getDescription(), pattern, method);
//		} catch (Exception e) {
//			return new PermissionRequest(null, operation.getOperationId(), operation.getSummary(),
//					operation.getDescription(), pattern, method);
//		}
//	}
//}
