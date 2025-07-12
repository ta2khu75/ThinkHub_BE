package com.ta2khu75.thinkhub.post.entity;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ta2khu75.quiz.model.AccessModifier;
import com.ta2khu75.quiz.model.entity.base.BaseEntityLong;
import com.ta2khu75.quiz.model.entity.base.SaltedIdentifiable;
import com.ta2khu75.quiz.util.SaltedType;
import com.ta2khu75.quiz.util.SqidsUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "author", "quizzes", "comments", "tags" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Blog extends BaseEntityLong implements SaltedIdentifiable {
	@Column(nullable = false, columnDefinition = "NVARCHAR(255)")
	String title;
	@Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
	String content;
	int viewCount;
	boolean deleted;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	AccessModifier accessModifier;

	@OneToMany(mappedBy = "blog", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	Set<Quiz> quizzes;
	@OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE, orphanRemoval = true)
	List<Comment> comments;
	@ManyToMany(cascade = { CascadeType.PERSIST })
	Set<BlogTag> tags;
	@ManyToOne
	AccountProfile author;

	public void addQuiz(Quiz quiz) {
		quizzes.add(quiz);
		quiz.setBlog(this);
	}

	public void removeQuiz(Quiz quiz) {
		quizzes.remove(quiz);
		quiz.setBlog(null);
	}

	@Override
	public SaltedType getSaltedType() {
		return SaltedType.BLOG;
	}
	public static Blog fromEncodedId(String encodedId) {
		Long idNumber = SqidsUtil.decodeWithSalt(encodedId, SaltedType.BLOG);
		Blog blog = new Blog();
		blog.setId(idNumber);
		return blog;
	}
}
