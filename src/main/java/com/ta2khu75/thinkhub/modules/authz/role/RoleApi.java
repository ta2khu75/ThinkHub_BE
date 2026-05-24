package com.ta2khu75.thinkhub.modules.authz.role;

import java.util.List;

import com.ta2khu75.thinkhub.modules.authz.api.dto.RoleSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface RoleApi extends CrudService<RoleRequest, RoleResponse, Long>, ExistsService<Long> {
	RoleResponse readByName(String name);

	List<RoleResponse> readAll();

	boolean exists(Long id);

	RoleSummary readSummaryByName(String name);

	RoleSummary readSummary(Long id);

	boolean existsByName(String name);
}
