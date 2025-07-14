package com.ta2khu75.thinkhub.result.dto;

import java.time.Instant;
import java.util.Set;

import com.ta2khu75.thinkhub.shared.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuizResultSearch extends Search {
	private Instant fromDate;
	private Instant toDate;
	private Set<Long> quizCategoryIds;
}
