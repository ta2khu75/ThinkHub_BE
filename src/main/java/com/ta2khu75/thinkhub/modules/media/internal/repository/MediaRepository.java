package com.ta2khu75.thinkhub.modules.media.internal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ta2khu75.thinkhub.modules.media.internal.domain.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	@Query("select m.url from Media m where m.id = :id")
	Optional<String> findUrlById(Long id);
}
