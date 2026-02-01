package com.ta2khu75.thinkhub.modules.quiz.internal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.quiz.api.enums.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.api.enums.ResultVisibility;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntityLong;
import com.ta2khu75.thinkhub.shared.domain.entity.HasIdSubject;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;
import com.ta2khu75.thinkhub.shared.util.SlugUtil;

@Data
@Entity
@AllArgsConstructor
@ToString(exclude = { "questions" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, exclude = { "questions" })
public class Quiz extends BaseEntityLong implements HasIdSubject {
	public Quiz() {
		super();
		shuffleQuestion = true;
		resultVisibility = ResultVisibility.FULL;
	}

	@Column(nullable = false, length = 255)
	String title;
	Integer duration;
	@Column(nullable = false, length = 255)
	String description;
	@Column(nullable = false)
	String slug;
	Long mediaId;
	boolean shuffleQuestion = true;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	QuizLevel level;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	QuizStatus status;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	ResultVisibility resultVisibility;

	@Column(nullable = false, updatable = false)
	Long ownerId;
	@Column(nullable = false)
	Long categoryId;
	@ElementCollection
	Set<Long> postIds;
	@ElementCollection
	@Column(nullable = false)
	Set<Long> tagIds;
	@JoinColumn(name = "quiz_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Question> questions;

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.QUIZ;
	}

	@PrePersist
	public void prePersist() {
		slug = SlugUtil.toSlug(title);
	}
}
