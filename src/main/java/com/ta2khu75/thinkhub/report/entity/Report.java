package com.ta2khu75.thinkhub.report.entity;

import com.ta2khu75.quiz.model.TargetType;
import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.ReportType;
import com.ta2khu75.quiz.model.entity.base.BaseEntityCustom;
import com.ta2khu75.quiz.model.entity.base.SaltedIdentifiable;
import com.ta2khu75.quiz.model.entity.id.ReportId;
import com.ta2khu75.quiz.util.SaltedType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report extends BaseEntityCustom<ReportId> implements SaltedIdentifiable {
	@ManyToOne
	@MapsId("authorId")
	AccountProfile author;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ReportType type;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ReportStatus status= ReportStatus.PENDING;

	@Override
	public SaltedType getSaltedType() {
		switch (getId().getTargetType()) {
		case QUIZ:
			return SaltedType.QUIZ;
		case BLOG:
			return SaltedType.BLOG;
		default:
			throw new IllegalArgumentException("Unexpected value: " + getId().getTargetType());
		}
	}

}
