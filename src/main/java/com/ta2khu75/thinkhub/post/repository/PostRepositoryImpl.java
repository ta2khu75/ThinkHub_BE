package com.ta2khu75.thinkhub.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ta2khu75.thinkhub.post.dto.PostSearch;
import com.ta2khu75.thinkhub.post.entity.Post;
import com.ta2khu75.thinkhub.post.entity.QPost;

import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.getOrderSpecifiers;

import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.applyIfNotNull;
import static com.ta2khu75.thinkhub.shared.util.QueryDslUtil.applyIfNotEmpty;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> search(PostSearch search) {
		QPost post = QPost.post;
		Pageable pageable = search.toPageable();
		List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, post);
		Predicate[] conditions = new Predicate[] {
				applyIfNotNull(search.getKeyword(), () -> post.title.containsIgnoreCase(search.getKeyword())),
				applyIfNotEmpty(search.getTagIds(), () -> post.tagIds.any().in(search.getTagIds())),
				applyIfNotEmpty(search.getCategoryIds(), () -> post.categoryId.in(search.getTagIds())),
				applyIfNotNull(search.getMinView(), () -> post.viewCount.goe(search.getMinView())),
				applyIfNotNull(search.getMaxView(), () -> post.viewCount.loe(search.getMaxView())),
				applyIfNotNull(search.getAccessModifier(), () -> post.accessModifier.eq(search.getAccessModifier())),
				applyIfNotNull(search.getAuthorIdQuery(), () -> post.authorId.eq(search.getAuthorIdQuery())) };
		JPAQuery<Post> query = queryFactory.selectFrom(post).where(conditions)
				.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])).offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		List<Post> content = query.fetch();

		// 3. Đếm tổng số bản ghi
		long total = queryFactory.select(post.count()).from(post).where(conditions).fetchOne();
		return new PageImpl<>(content, pageable, total);
	}

}
