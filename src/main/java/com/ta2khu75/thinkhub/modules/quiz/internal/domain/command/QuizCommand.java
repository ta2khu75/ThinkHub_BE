package com.ta2khu75.thinkhub.modules.quiz.internal.domain.command;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.api.model.ResultVisibility;
import com.ta2khu75.thinkhub.modules.quiz.internal.domain.Question;

public record QuizCommand(String title, String slug, String description, Integer duration, Long ownerId, Long imageId,
		Long categoryId, Set<Long> tagIds, Set<Long> postIds, List<Question> questions, boolean shuffleQuestion,
		ResultVisibility resultVisibility, QuizLevel level) {

}
