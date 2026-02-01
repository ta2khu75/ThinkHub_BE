package com.ta2khu75.thinkhub.modules.report.required.port;

import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;

public interface ReportUserPort {
	AuthorResponse readAuthor(Long id);
}
