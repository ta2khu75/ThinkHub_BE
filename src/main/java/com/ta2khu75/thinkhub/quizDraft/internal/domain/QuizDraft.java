package com.ta2khu75.thinkhub.quizDraft.internal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizDraft {
	String id;
	String title;
	Integer duration;
	String description;
	Long ownerId;
	Long mediaId;
	QuizLevel level;
	Long categoryId;
	List<String> tagNames;
	List<QuestionDraft> questions;
	Instant lastModifiedAt;
}