package com.ta2khu75.thinkhub.tag.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);
	List<Tag> findAllByNameIn(Collection<String> names);
	Optional<Tag> findByName(String name);
}
