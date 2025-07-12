package com.ta2khu75.thinkhub.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.config.IdProperties.IdType;
import com.ta2khu75.thinkhub.quiz.enums.QuizLevel;
import com.ta2khu75.thinkhub.quiz.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.entity.IdTypeProvider;
import com.ta2khu75.thinkhub.shared.enums.AccessModifier;

@Entity
@Data
@AllArgsConstructor
@ToString(exclude = { "questions" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, exclude = { "questions" })
public class Quiz extends BaseEntityLong implements IdTypeProvider {
	public Quiz() {
		accessModifier = AccessModifier.PRIVATE;
		resultVisibility = ResultVisibility.FULL;
		shuffleQuestion = true;
	}

	@Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
	String title;
	@Column(nullable = false)
	Integer duration;
	@Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
	String description;
	@Column(nullable = false)
	String imagePath;
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
	Long accountId;
	Long blogId;
	Long categoryId;
	Set<Long> tagIds;
	@OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Question> questions;

	@Override
	public IdType getIdType() {
		return IdType.QUIZ;
	}
}
