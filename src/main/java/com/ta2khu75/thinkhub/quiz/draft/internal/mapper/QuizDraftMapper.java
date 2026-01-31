package com.ta2khu75.thinkhub.quiz.draft.internal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftCreateRequest;
import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftResponse;
import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuizDraftUpdateRequest;
import com.ta2khu75.thinkhub.quiz.draft.internal.domain.QuizDraft;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

import org.mapstruct.Mapping;

@Mapper(config = MapperSpringConfig.class)
public interface QuizDraftMapper extends Converter<QuizDraft, QuizDraftResponse> {
	@Override
	@Mapping(target = "imageUrl", ignore = true)
	QuizDraftResponse convert(QuizDraft value);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "title", source = "title")
	@Mapping(target = "categoryId", source = "categoryId")
	QuizDraft create(QuizDraftCreateRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ownerId", ignore = true)
	@Mapping(target = "lastModifiedAt", ignore = true)
	void update(QuizDraftUpdateRequest request, @MappingTarget QuizDraft quiz);

}
