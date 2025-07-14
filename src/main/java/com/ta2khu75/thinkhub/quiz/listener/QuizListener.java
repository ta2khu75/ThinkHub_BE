package com.ta2khu75.thinkhub.quiz.listener;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.quiz.QuizService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuizListener {
	private final QuizService service;
//	@EventListener
//	public void checkCategoryExists(CheckExistsEvent<Quiz, Long> event) {
//		service.checkExists(event.id());
//	}
}
