package com.ta2khu75.thinkhub.authz.internal.permission;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionService extends CrudService<PermissionRequest, PermissionResponse, Long> {
	Set<PermissionResponse> saveAll(Set<PermissionRequest> requests);
	Set<PermissionResponse> readAllBySummary(Set<String> summaries);
	PermissionResponse readBySummary(String summary);
	PermissionResponse readByPatternAndMethod(String pattern, RequestMethod method);
//	Optional<PermissionResponse> findBySummary(String summary);
}
