package com.ta2khu75.thinkhub.quiz.dto;

import java.util.List;

import com.ta2khu75.thinkhub.quiz.enums.QuizLevel;
import com.ta2khu75.thinkhub.shared.dto.Search;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuizSearch extends Search {
	private List<QuizLevel> levels;
	private List<Long> categories;
	private Integer minDuration;
	private Integer maxDuration;
	private Long authorId;
	private Boolean completed;
	private AccessModifier accessModifier;
}
