package com.ta2khu75.thinkhub.account.repository;

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
import com.ta2khu75.thinkhub.account.dto.request.AccountSearch;
import com.ta2khu75.thinkhub.account.entity.Account;
import com.ta2khu75.thinkhub.account.entity.QAccount;
import com.ta2khu75.thinkhub.account.projection.Author;

import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.getOrderSpecifiers;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.applyIfNotNull;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Account> search(AccountSearch search) {
		QAccount account = QAccount.account;
		Pageable pageable = search.toPageable();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, account);
		List<Predicate> conditionList = Stream
				.of(applyIfNotNull(search.getRoleId(), () -> account.status.roleId.eq(search.getRoleId())),
						applyIfNotNull(search.getEnabled(), () -> account.status.enabled.eq(search.getEnabled())),
						applyIfNotNull(search.getNonLocked(), () -> account.status.nonLocked.eq(search.getNonLocked())),
						applyIfNotNull(search.getCreatedFrom(), () -> account.createdAt.goe(search.getCreatedFrom())),
						applyIfNotNull(search.getCreatedTo(), () -> account.createdAt.loe(search.getCreatedTo())))
				.filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		if (search.getKeyword() != null && search.getKeyword().startsWith("@")) {
			conditionList.add(account.email.contains(search.getKeyword().substring(1)));
		} else {
			conditionList.add(applyIfNotNull(search.getKeyword(),
					() -> account.profile.displayName.contains(search.getKeyword())));
		}
		Predicate[] conditions = conditionList.toArray(new Predicate[0]);
		JPAQuery<Account> query = queryFactory.selectFrom(account).where(conditions)
				.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])).offset(pageable.getOffset())
				.limit(pageable.getPageSize());
		// 2. Fetch kết quả
		List<Account> content = query.fetch();

		// 3. Đếm tổng số bản ghi
		long total = queryFactory.select(account.count()).from(account).where(conditions).fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public List<Author> findAllAuthorsByAccountIds(Collection<Long> accountIds) {
		QAccount account = QAccount.account;
		List<Predicate> conditionList = Stream.of(applyIfNotNull(accountIds, () -> account.id.in(accountIds)))
				.filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		Predicate[] conditions = conditionList.toArray(new Predicate[0]);
		JPAQuery<Author> query = queryFactory
				.select(Projections.constructor(Author.class, account.id, account.profile.displayName)).from(account)
				.where(conditions);
		return query.fetch();
	}

	@Override
	public Optional<Author> findAuthorByAccountId(Long accountId) {
		QAccount account = QAccount.account;
		Predicate[] conditions = new Predicate[] { account.id.eq(accountId) };
		JPAQuery<Author> query = queryFactory
				.select(Projections.constructor(Author.class, account.id, account.profile.displayName)).from(account)
				.where(conditions);
		Author content = query.fetchOne();
		return Optional.ofNullable(content);
	}

}