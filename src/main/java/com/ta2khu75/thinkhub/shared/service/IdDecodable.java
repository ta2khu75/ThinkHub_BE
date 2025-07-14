package com.ta2khu75.thinkhub.shared.service;

import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.util.IdConverterUtil;

public interface IdDecodable {
	default Long decodeIdWithConfig(String id, IdConfig idConfig) {
		return IdConverterUtil.decode(id, idConfig);
	}

	default Long decodeAccountId(String accountId) {
		return IdConverterUtil.decode(accountId, IdConfig.ACCOUNT);
	}
	default Long decodeId(String id) {
		return decodeIdWithConfig(id, getIdConfig());
	}
	IdConfig getIdConfig(); 
}
