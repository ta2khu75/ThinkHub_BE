package com.ta2khu75.thinkhub.authz.internal.permission;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionResponse;

public interface PermissionService {
	List<PermissionSummary> saveAll(Collection<PermissionSummary> permissions);

	Set<PermissionSummary> readAllSummaryByCodes(Collection<String> codes);

	PermissionResponse readByCode(String code);

	PermissionResponse read(Long id);
}
