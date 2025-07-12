package com.ta2khu75.thinkhub.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.quiz.dto.QuestionDto;
import com.ta2khu75.thinkhub.quiz.entity.Question;

@Mapper(componentModel = "spring", uses = { AnswerMapper.class })
public interface QuestionMapper extends Converter<Question, QuestionDto> {
	@Override
	QuestionDto convert(Question source);

	@Mapping(target = "answers", source = "answers", qualifiedByName = "toAnswer")
	Question toEntity(QuestionDto dto);
}
