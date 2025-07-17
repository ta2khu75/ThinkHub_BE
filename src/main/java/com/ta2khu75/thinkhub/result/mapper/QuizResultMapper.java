package com.ta2khu75.thinkhub.result.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.dto.UserAnswerRequest;
import com.ta2khu75.thinkhub.result.dto.UserAnswerResponse;
import com.ta2khu75.thinkhub.result.entity.QuizResult;
import com.ta2khu75.thinkhub.result.entity.UserAnswer;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface QuizResultMapper
		extends Converter<QuizResult, QuizResultResponse>, PageMapper<QuizResult, QuizResultResponse> {
	@Override
	@Mapping(target = "id", source = "source")
	@Mapping(target = "quiz", ignore = true)
	@Mapping(target = "account", ignore = true)
	QuizResultResponse convert(QuizResult source);

	UserAnswerResponse toDto(UserAnswer userAnswer);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "correct", ignore = true)
	UserAnswer toEntity(UserAnswerRequest request);
}
