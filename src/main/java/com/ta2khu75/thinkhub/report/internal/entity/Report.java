package com.ta2khu75.thinkhub.report.internal.entity;

import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.report.internal.enums.ReportType;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;

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
public class Report extends BaseEntityLong {
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
