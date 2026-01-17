package com.ta2khu75.thinkhub.report.required.port.client;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.report.required.port.ReportUserPort;
import com.ta2khu75.thinkhub.shared.api.controller.BaseClient;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
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
