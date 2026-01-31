package com.ta2khu75.thinkhub.quiz.internal.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.quiz.draft.api.dto.QuestionDto;
import com.ta2khu75.thinkhub.quiz.internal.entity.Question;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface QuestionMapper extends Converter<Question, QuestionDto> {
	@Override
	QuestionDto convert(Question source);

	Question toEntity(QuestionDto dto);
}
