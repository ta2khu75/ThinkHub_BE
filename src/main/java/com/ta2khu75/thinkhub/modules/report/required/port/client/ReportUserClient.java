package com.ta2khu75.thinkhub.modules.report.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.report.required.port.ReportUserPort;
import com.ta2khu75.thinkhub.modules.user.api.UserApi;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

@Component
public class ReportUserClient extends BaseClient<UserApi> implements ReportUserPort {

	protected ReportUserClient(UserApi api) {
		super(api);
	}

	@Override
	public AuthorResponse readAuthor(Long id) {
		return api.readAuthor(id);
	}

}
