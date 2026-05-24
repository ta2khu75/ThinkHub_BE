package com.ta2khu75.thinkhub.modules.report.internal.domain;

import com.ta2khu75.thinkhub.modules.report.api.model.ReportTargetType;
import com.ta2khu75.thinkhub.modules.report.api.model.ReportType;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report extends BaseEntity {
	public Report() {
		super();
		this.status = ReportStatus.PENDING;
	}

	Long authorId;
	String targetId;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ReportTargetType targetType;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ReportType type;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ReportStatus status = ReportStatus.PENDING;
}
