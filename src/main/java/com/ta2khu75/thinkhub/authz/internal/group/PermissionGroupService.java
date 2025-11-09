package com.ta2khu75.thinkhub.authz.internal.group;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionGroupResponse;

public interface PermissionGroupService {
	List<PermissionGroupResponse> readAll();

	Set<PermissionGroupSummary> readAllSummaryByCodes(Collection<String> codes);

	List<PermissionGroupSummary> saveAll(Collection<PermissionGroupSummary> requests);
}
