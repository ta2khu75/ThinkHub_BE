package com.ta2khu75.thinkhub.modules.category.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta2khu75.thinkhub.modules.category.internal.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
