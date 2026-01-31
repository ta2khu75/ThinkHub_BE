package com.ta2khu75.thinkhub.report.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.report.required.port.ReportUserPort;
import com.ta2khu75.thinkhub.shared.common.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.user.api.UserApi;

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
