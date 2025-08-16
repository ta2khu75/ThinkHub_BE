package com.ta2khu75.thinkhub.authority.api;

import java.util.List;

import com.ta2khu75.thinkhub.authority.api.dto.RoleDto;
import com.ta2khu75.thinkhub.authority.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;
import com.ta2khu75.thinkhub.shared.service.ExistsService;

public interface RoleApi extends CrudService<RoleRequest, RoleResponse, Long> , ExistsService<Long>{
	RoleResponse readByName(String name);
	List<RoleResponse> readAll();
	boolean exists(Long id);
	RoleDto readDtoByName(String name);
	RoleDto readDto(Long id);
	boolean existsByName(String name);
}
