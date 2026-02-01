package com.ta2khu75.thinkhub.modules.report.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.report.internal.entity.Report;
import com.ta2khu75.thinkhub.modules.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.shared.exception.BusinessException;

@Component
public class ReportValidator {
	public void validateUpdate(Report report) {
		if (report.getStatus() != ReportStatus.PENDING) {
			throw new BusinessException(ReportErrorCode.UPDATE_NOT_ALLOWED,
					"Cannot update report with status " + report.getStatus());
		}
	}
}
