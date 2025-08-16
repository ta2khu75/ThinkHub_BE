package com.ta2khu75.thinkhub.quiz.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.shared.api.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

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
	private String authorId;
	private Long authorIdQuery;
	private Boolean completed;
	private AccessModifier accessModifier;
	private LocalDate createdFrom;
	private LocalDate createdTo;
}
