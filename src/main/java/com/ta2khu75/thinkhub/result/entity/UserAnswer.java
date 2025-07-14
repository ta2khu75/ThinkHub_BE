package com.ta2khu75.thinkhub.result.entity;

import java.util.Set;

import com.ta2khu75.thinkhub.quiz.entity.Answer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(updatable = false)
	boolean correct;
	@Column(nullable = false, updatable = false)
	Long questionId;
	@Column(nullable = false, updatable = false)
	Set<Long> answerIds;
}
