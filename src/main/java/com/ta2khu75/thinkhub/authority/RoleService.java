package com.ta2khu75.thinkhub.authority;

import java.util.List;

import com.ta2khu75.thinkhub.authority.request.RoleRequest;
import com.ta2khu75.thinkhub.authority.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface RoleService extends CrudService<RoleRequest, RoleResponse, Long> {
	RoleResponse readByName(String name);
	RoleDto readDtoByName(String name);
	RoleDto readDto(Long id);
	List<RoleResponse> readAll();
	boolean exists(Long id);
}
