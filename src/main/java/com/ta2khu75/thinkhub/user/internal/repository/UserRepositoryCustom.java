package com.ta2khu75.thinkhub.user.internal.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.user.api.dto.UserSearch;
import com.ta2khu75.thinkhub.user.internal.entity.User;
import com.ta2khu75.thinkhub.user.projection.internal.projection.Author;

public interface UserRepositoryCustom {
	Page<User> search(UserSearch search);

	List<Author> findAllAuthorsByUserIds(Collection<Long> userIds);

	Optional<Author> findAuthorByUserId(Long userId);
}