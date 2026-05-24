package com.ta2khu75.thinkhub.modules.quiz.internal.domain;

import com.ta2khu75.thinkhub.modules.quiz.internal.validator.AnswerErrorCode;
import com.ta2khu75.thinkhub.shared.util.Guard;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Answer {
	protected Answer() {
	}

	public static Answer create(String content, boolean correct) {
		Guard.notBlank(content, AnswerErrorCode.INVALID_CONTENT, "content");
		Answer answer = new Answer();
		answer.content = content;
		answer.correct = correct;
		return answer;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(255)")
	String content;
	@Column(nullable = false)
	boolean correct;
}
