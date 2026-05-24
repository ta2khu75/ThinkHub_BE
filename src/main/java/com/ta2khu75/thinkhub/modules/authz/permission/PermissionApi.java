package com.ta2khu75.thinkhub.modules.authz.permission;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.PermissionResponse;

public interface PermissionApi {
	List<PermissionSummary> saveAll(Collection<PermissionSummary> permissions);

	Set<PermissionSummary> readAllSummaryByCodes(Collection<String> codes);

	PermissionResponse readByCode(String code);

	PermissionResponse read(Long id);
}
