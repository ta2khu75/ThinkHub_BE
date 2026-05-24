package com.ta2khu75.thinkhub.modules.quiz.result.internal.entity;

import java.time.Instant;
import java.util.Set;

import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;
import com.ta2khu75.thinkhub.shared.domain.entity.HasPublicId;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "userAnswers" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, exclude = { "userAnswers" })
public class QuizResult extends BaseEntity implements HasPublicId {
	Float score;
	Integer correctCount;
	Instant endTime;
	@Column(nullable = false, updatable = false)
	Long userId;
	@Column(nullable = false, updatable = false)
	Long quizId;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "quiz_result_id")
	Set<UserAnswer> userAnswers;

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.QUIZ_RESULT;
	}
}
