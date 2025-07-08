package com.ta2khu75.thinkhub.authority;

import java.util.Optional;
import java.util.Set;

import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionService extends CrudService<PermissionRequest, PermissionResponse, Long> {
	Set<PermissionResponse> readAllBySummary(Set<String> summaries);
	PermissionResponse readBySummary(String summary);
	Optional<PermissionResponse> findBySummary(String summary);
}
