package com.ta2khu75.thinkhub.report.required.port;

import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;

public interface ReportUserPort {
	AuthorResponse readAuthor(Long id);
}
