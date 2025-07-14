package com.ta2khu75.thinkhub.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.quiz.dto.QuestionDto;
import com.ta2khu75.thinkhub.quiz.entity.Question;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface QuestionMapper extends Converter<Question, QuestionDto> {
	@Override
	QuestionDto convert(Question source);

	Question toEntity(QuestionDto dto);
}
