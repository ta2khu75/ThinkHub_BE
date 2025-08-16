package com.ta2khu75.thinkhub.result.internal.entity;

import java.time.Instant;
import java.util.Set;

import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;

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
public class QuizResult extends BaseEntityLong {
	Float score;
	Integer correctCount;
	Instant endTime;
	@Column(nullable = false, updatable = false)
	Long accountId;
	@Column(nullable = false, updatable = false)
	Long quizId;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "quiz_result_id")
	Set<UserAnswer> userAnswers;
}
