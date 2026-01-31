package com.ta2khu75.thinkhub.quiz.draft.internal.domain;

import lombok.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import com.ta2khu75.thinkhub.quiz.api.enums.QuestionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDraft {
	String content;
	Long mediaId;
	QuestionType type;
	List<AnswerDraft> answers;
	boolean shuffleAnswer;
}
