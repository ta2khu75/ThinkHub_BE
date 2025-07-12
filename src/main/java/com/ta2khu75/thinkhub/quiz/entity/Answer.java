package com.ta2khu75.thinkhub.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
	String content;
	@Column(nullable = false)
	boolean correct;
}
