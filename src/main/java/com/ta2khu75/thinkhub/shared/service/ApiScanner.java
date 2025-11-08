package com.ta2khu75.thinkhub.shared.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.config.PublicEndpointRegistry;
import com.ta2khu75.thinkhub.shared.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Getter
public class ApiScanner {
	private final RequestMappingHandlerMapping handlerMapping;
	private final PublicEndpointRegistry publicEndpointRegistry;
	private Map<String, Map<String, PermissionSummary>> permissionMap = new HashMap<>();
	private Map<String, PermissionGroupSummary> permissionGroupMap = new HashMap<>();

	@PostConstruct
	public void init() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo mappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();
			Method method = handlerMethod.getMethod();
			Operation operation = method.getAnnotation(Operation.class);
			Tag tag = handlerMethod.getBeanType().getAnnotation(Tag.class);
			String pattern = getPattern(mappingInfo);
			if (operation != null && tag != null && pattern != null) {
				String tagCode = this.getTagCode(tag);
				RequestMethod requestMethod = mappingInfo.getMethodsCondition().getMethods().iterator().next();
				// map permission
				PermissionSummary permission = mapPermission(tagCode, operation, method, requestMethod, pattern);
				permissionMap.computeIfAbsent(tagCode, k -> new HashMap<>()).put(permission.getCode(), permission);
				// map permission group
				PermissionGroupSummary permissionGroup = mapPermissionGroup(tagCode, tag);
				permissionGroupMap.putIfAbsent(tagCode, permissionGroup);
			}
		}
	}

	private String getPattern(RequestMappingInfo mappingInfo) {
		PathPatternsRequestCondition condition = mappingInfo.getPathPatternsCondition();
		if (condition != null && !condition.getPatternValues().isEmpty()) {
			return condition.getPatternValues().iterator().next();
		}
		return null;
	}

	private PermissionGroupSummary mapPermissionGroup(String tagCode, Tag tag) {
		return new PermissionGroupSummary(null, tagCode, tag.name().toLowerCase(), tag.description(), new HashSet<>());
	}

	private PermissionSummary mapPermission(String tagName, Operation operation, Method method,
			RequestMethod requestMethod, String pattern) {
		String code = this.getPermissionCode(tagName, operation, method);
		String displayName = this.getFormat(tagName, operation.summary());
		String description = this.getFormat(tagName, operation.description());
		return new PermissionSummary(null, code, displayName, description, pattern, requestMethod);
	}

	private String getFormat(String tag, String content) {
		if (content == null || content.isBlank() || !content.contains("%s")) {
			return content;
		}
		return String.format(content, tag.toLowerCase());
	}

	private String getPermissionCode(String tagName, Operation operation, Method method) {
		String opId = operation.operationId();
		if (opId == null || opId.isBlank()) {
			opId = StringUtil.toUpperSnakeCase(method.getName().toUpperCase());
		}
		return tagName + "::" + opId.toUpperCase();
	}

	private String getTagCode(Tag tag) {
		return tag.name().replace(" ", "_").toUpperCase();
	}

}
