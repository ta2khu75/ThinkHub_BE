package com.ta2khu75.thinkhub.user.internal.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.internal.entity.QUser;
import com.ta2khu75.thinkhub.user.internal.entity.User;
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.getOrderSpecifiers;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.applyIfNotNull;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.toInstant;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<User> search(UserSearch search) {
		QUser user = QUser.user;
		Pageable pageable = search.toPageable();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, user);
		List<Predicate> conditionList = Stream.of(
				applyIfNotNull(search.getRoleId(), () -> user.status.roleId.eq(search.getRoleId())),
				applyIfNotNull(search.getEnabled(), () -> user.status.enabled.eq(search.getEnabled())),
				applyIfNotNull(search.getNonLocked(), () -> user.status.nonLocked.eq(search.getNonLocked())),
				applyIfNotNull(search.getCreatedFrom(), () -> user.createdAt.goe(toInstant(search.getCreatedFrom()))),
				applyIfNotNull(search.getCreatedTo(), () -> user.createdAt.lt(toInstant(search.getCreatedTo()))),
				applyIfNotNull(search.getUpdatedFrom(), () -> user.updatedAt.goe(toInstant(search.getUpdatedFrom()))),
				applyIfNotNull(search.getUpdatedTo(), () -> user.updatedAt.lt(toInstant(search.getUpdatedTo()))))
				.filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		if (search.getKeyword() != null && search.getKeyword().startsWith("@")) {
			conditionList.add(user.email.contains(search.getKeyword().substring(1)));
		} else {
			conditionList.add(applyIfNotNull(search.getKeyword(), () -> user.username.contains(search.getKeyword())));
		}
		Predicate[] conditions = conditionList.toArray(new Predicate[0]);
		JPAQuery<User> query = queryFactory.selectFrom(user).where(conditions)
				.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])).offset(pageable.getOffset())
				.limit(pageable.getPageSize());
		// 2. Fetch kết quả
		List<User> content = query.fetch();

		// 3. Đếm tổng số bản ghi
		long total = queryFactory.select(user.count()).from(user).where(conditions).fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public List<Author> findAllAuthorsByUserIds(Collection<Long> userIds) {
		QUser user = QUser.user;
		List<Predicate> conditionList = Stream.of(applyIfNotNull(userIds, () -> user.id.in(userIds)))
				.filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		Predicate[] conditions = conditionList.toArray(new Predicate[0]);
		JPAQuery<Author> query = queryFactory.select(Projections.constructor(Author.class, user.id, user.username))
				.from(user).where(conditions);
		return query.fetch();
	}

	@Override
	public Optional<Author> findAuthorByUserId(Long userId) {
		QUser user = QUser.user;
		Predicate[] conditions = new Predicate[] { user.id.eq(userId) };
		JPAQuery<Author> query = queryFactory.select(Projections.constructor(Author.class, user.id, user.username))
				.from(user).where(conditions);
		Author content = query.fetchOne();
		return Optional.ofNullable(content);
	}

}