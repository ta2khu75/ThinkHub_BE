package com.ta2khu75.thinkhub.quiz.internal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.api.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdConfigProvider;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;

@Entity
@Data
@AllArgsConstructor
@ToString(exclude = { "questions" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, exclude = { "questions" })
public class Quiz extends BaseEntityLong implements IdConfigProvider {
	public Quiz() {
		super();
		accessModifier = AccessModifier.PRIVATE;
		resultVisibility = ResultVisibility.FULL;
		shuffleQuestion = true;
	}

	@Column(nullable = false, length = 255)
	String title;
	@Column(nullable = false)
	Integer duration;
	@Column(nullable = false, length = 255)
	String description;
	@Column(nullable = false)
	String imageUrl;
	boolean shuffleQuestion = true;
	boolean deleted;
	boolean completed;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	QuizLevel level;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	AccessModifier accessModifier;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ResultVisibility resultVisibility;

	@Column(nullable = false, updatable = false)
	Long authorId;
	@ElementCollection
	Set<Long> postIds;
	@ElementCollection
	@Column(nullable = false)
	Set<Long> tagIds;
	@Column(nullable = false)
	Long categoryId;
	@JoinColumn(name = "quiz_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Question> questions;

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.QUIZ;
	}
}
