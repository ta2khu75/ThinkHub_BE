package com.ta2khu75.thinkhub.quiz.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.internal.entity.QuizStatus;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
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
