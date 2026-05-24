package com.ta2khu75.thinkhub.modules.authz.group;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.PermissionGroupResponse;

public interface PermissionGroupApi {
	List<PermissionGroupResponse> readAll();

	Set<PermissionGroupSummary> readAllSummaryByCodes(Collection<String> codes);

	List<PermissionGroupSummary> saveAll(Collection<PermissionGroupSummary> requests);
}
