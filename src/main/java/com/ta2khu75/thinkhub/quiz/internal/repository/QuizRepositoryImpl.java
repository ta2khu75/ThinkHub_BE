package com.ta2khu75.thinkhub.quiz.internal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ta2khu75.thinkhub.quiz.api.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.internal.entity.QQuiz;
import com.ta2khu75.thinkhub.quiz.internal.entity.Quiz;

import lombok.RequiredArgsConstructor;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.getOrderSpecifiers;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.applyIfNotNull;

@Repository
@RequiredArgsConstructor
public class QuizRepositoryImpl implements QuizRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Quiz> search(QuizSearch search) {
		QQuiz quiz = QQuiz.quiz;
		Pageable pageable = search.toPageable();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, quiz);
		Predicate[] conditions = new Predicate[] {
				applyIfNotNull(search.getKeyword(), () -> quiz.title.contains(search.getKeyword())),
				applyIfNotNull(search.getCategoryIds(), () -> quiz.categoryId.in(search.getCategoryIds())),
				applyIfNotNull(search.getTagIds(), () -> quiz.tagIds.any().in(search.getCategoryIds())),
				applyIfNotNull(search.getLevels(), () -> quiz.level.in(search.getLevels())),
				applyIfNotNull(search.getCompleted(), () -> quiz.completed.eq(search.getCompleted())),
				applyIfNotNull(search.getMinDuration(), () -> quiz.duration.goe(search.getMinDuration())),
				applyIfNotNull(search.getMaxDuration(), () -> quiz.duration.loe(search.getMaxDuration())),
				applyIfNotNull(search.getAccessModifier(), () -> quiz.accessModifier.eq(search.getAccessModifier())),
				applyIfNotNull(search.getAuthorIdQuery(), () -> quiz.authorId.eq(search.getAuthorIdQuery())) };
		JPAQuery<Quiz> query = queryFactory.selectFrom(quiz).leftJoin(quiz.tagIds).fetchJoin().where(conditions)
				.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])).offset(pageable.getOffset())
				.limit(pageable.getPageSize());
		List<Quiz> content = query.fetch();

		long total = queryFactory.select(quiz.count()).from(quiz).where(conditions).fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

}
