package com.ta2khu75.thinkhub.report.entity;

import java.io.Serializable;

import com.ta2khu75.quiz.model.TargetType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ReportId implements Serializable {
	private static final long serialVersionUID = -6196953391029199691L;
	private Long authorId;
	private Long targetId;
	@Enumerated(EnumType.STRING)
	@NotNull
	private TargetType targetType;
 }