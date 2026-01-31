package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;

public interface IdDecodable {
	default Long decodeId(String id, IdConfig idConfig) {
		return IdConverterUtil.decode(id, idConfig);
	}

	default Long decodeId(String id) {
		return decodeId(id, getIdConfig());
	}

	IdConfig getIdConfig();
}
