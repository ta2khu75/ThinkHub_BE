package com.ta2khu75.thinkhub.modules.quiz.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ta2khu75.thinkhub.modules.quiz.api.model.QuizLevel;
import com.ta2khu75.thinkhub.modules.quiz.api.model.ResultVisibility;
import com.ta2khu75.thinkhub.modules.quiz.internal.validator.QuizErrorCode;
import com.ta2khu75.thinkhub.shared.domain.entity.BaseEntity;
import com.ta2khu75.thinkhub.shared.domain.entity.HasPublicId;
import com.ta2khu75.thinkhub.shared.domain.enums.IdSubject;
import com.ta2khu75.thinkhub.shared.util.Guard;
import com.ta2khu75.thinkhub.shared.util.SlugUtil;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quiz extends BaseEntity implements HasPublicId {
	protected Quiz() {
	}

	public static Quiz create(String title, String slug, String description, Integer duration, Long ownerId,
			Long imageId, Long categoryId, Set<Long> tagIds, Set<Long> postIds, List<Question> questions,
			boolean shuffleQuestion, ResultVisibility resultVisibility, QuizLevel level) {
		Guard.notBlank(title, QuizErrorCode.INVALID_TITLE, "title");
		Guard.notBlank(slug, QuizErrorCode.INVALID_SLUG, "slug");
		Guard.notBlank(description, QuizErrorCode.INVALID_DESCRIPTION, "description");
		Guard.minIfPresent(duration, 5, QuizErrorCode.INVALID_DURATION, "duration");
		Guard.notNull(level, QuizErrorCode.INVALID_LEVEL, "level");
		Guard.notNull(ownerId, QuizErrorCode.INVALID_OWNER, "ownerId");
		Guard.notNull(categoryId, QuizErrorCode.INVALID_CATEGORY, "categoryId");
		Guard.notEmpty(tagIds, QuizErrorCode.INVALID_QUESTION, "tagIds");
		Guard.notEmpty(questions, QuizErrorCode.INVALID_QUESTION, "questions");
		Guard.notNull(resultVisibility, QuizErrorCode.INVALID_RESULT_VISIBILITY, "resultVisibility");
		Quiz quiz = new Quiz();
		quiz.title = title;
		quiz.slug = slug;
		quiz.description = description;
		quiz.duration = duration;
		quiz.ownerId = ownerId;
		quiz.imageId = imageId;
		quiz.categoryId = categoryId;
		quiz.shuffleQuestion = shuffleQuestion;
		quiz.level = level;
		quiz.status = QuizStatus.ACTIVE;
		quiz.resultVisibility = resultVisibility;
		quiz.tagIds = tagIds;
		quiz.postIds = postIds;
		quiz.questions = questions;
		return quiz;
	}

	@Column(nullable = false, length = 255)
	String title;
	Integer duration;
	@Column(nullable = false, length = 255)
	String description;
	@Column(nullable = false)
	String slug;
	Long imageId;
	boolean shuffleQuestion;
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
	@OrderColumn(name = "position")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Question> questions;

	public void addQuestion(Question question) {
		Guard.notNull(question, QuizErrorCode.INVALID_QUESTION, "question");
		questions.add(question);
	}

	public void removeQuestion(Long questionId) {
		questions.removeIf(q -> q.getId().equals(questionId));
	}

	@Override
	public IdSubject getIdSubject() {
		return IdSubject.QUIZ;
	}

}
