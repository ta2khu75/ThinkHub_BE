package com.ta2khu75.thinkhub.modules.quiz.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuestionType;
import com.ta2khu75.thinkhub.modules.quiz.internal.validator.QuestionErrorCode;
import com.ta2khu75.thinkhub.shared.util.Guard;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
	protected Question() {
	}

	public static Question create(String content, Long imageId, boolean shuffleAnswer, QuestionType type,
			List<Answer> answers) {
		Guard.notBlank(content, QuestionErrorCode.INVALID_CONTENT, "content");
		Guard.notEmpty(answers, QuestionErrorCode.INVALID_ANSWERS, "answers");
		long correctCount = answers.stream().filter(Answer::isCorrect).count();
		Guard.condition(correctCount == 0, QuestionErrorCode.NO_CORRECT_ANSWER,
				"question must have at least one correct answer");
		Question question = new Question();
		question.content = content;
		question.imageId = imageId;
		question.shuffleAnswer = shuffleAnswer;
		question.type = type;
		question.answers = answers;
		return question;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(255)")
	String content;
	Long imageId;
	boolean shuffleAnswer;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	QuestionType type;
	@JoinColumn(name = "question_id")
	@OrderColumn(name = "position")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Answer> answers;
}
