package com.ta2khu75.thinkhub.quiz.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerDto {
	private Long id;
	@NotBlank
	private String content;
	private boolean correct;
}
