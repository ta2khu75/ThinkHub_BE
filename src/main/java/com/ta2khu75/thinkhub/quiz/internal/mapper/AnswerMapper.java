package com.ta2khu75.thinkhub.quiz.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.quiz.api.dto.AnswerDto;
import com.ta2khu75.thinkhub.quiz.internal.entity.Answer;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;


@Mapper(config = MapperSpringConfig.class)
public interface AnswerMapper extends Converter<Answer, AnswerDto>{
	@Named("toAnswer")
	Answer toEntity(AnswerDto request);

	@Override
	AnswerDto convert(Answer source);
}