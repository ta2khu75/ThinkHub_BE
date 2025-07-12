package com.ta2khu75.thinkhub.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import com.ta2khu75.thinkhub.quiz.enums.QuestionType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, columnDefinition = "VARCHAR(255)")
	String content;
	String imagePath;
	boolean shuffleAnswer;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	QuestionType type;
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Answer> answers;
}
