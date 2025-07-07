package com.ta2khu75.thinkhub.authority;

import com.ta2khu75.thinkhub.authority.request.PermissionRequest;
import com.ta2khu75.thinkhub.authority.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.service.CrudService;

public interface PermissionService extends CrudService<PermissionRequest, PermissionResponse, Long> {

}
