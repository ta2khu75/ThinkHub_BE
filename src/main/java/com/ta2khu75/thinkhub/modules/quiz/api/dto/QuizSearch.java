package com.ta2khu75.thinkhub.modules.quiz.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.internal.domain.QuizStatus;
import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuizSearch extends Search {
	private List<QuizLevel> levels;
	private List<Long> categoryIds;
	private List<Long> tagIds;
	private Integer minDuration;
	private Integer maxDuration;
	private String ownerId;
	private Long ownerIdQuery;
	private QuizStatus status;
	private LocalDate createdFrom;
	private LocalDate createdTo;
}
