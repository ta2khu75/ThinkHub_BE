package com.ta2khu75.thinkhub.category.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.category.CategoryService;
import com.ta2khu75.thinkhub.category.entity.Category;
import com.ta2khu75.thinkhub.shared.event.CheckExistsEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryListener {
	private final CategoryService service;

//	@EventListener
//	private void checkExists(CheckExistsEvent<Category, Long> event) {
//		service.checkExists(event.id());
//	}
}
