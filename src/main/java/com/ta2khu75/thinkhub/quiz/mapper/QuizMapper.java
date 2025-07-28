package com.ta2khu75.thinkhub.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;
import com.ta2khu75.thinkhub.tag.dto.TagDto;

import org.mapstruct.Mapping;

@Mapper(config = MapperSpringConfig.class)
public interface QuizMapper
		extends Converter<Quiz, QuizResponse>, BaseMapper<QuizRequest, Quiz>, PageMapper<Quiz, QuizResponse> {
	@Override
	@Mapping(target = "id", source = "source")
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "tags", source = "tagIds")
	QuizResponse convert(Quiz source);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tagIds", ignore = true)
	@Mapping(target = "postIds", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "authorId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Quiz toEntity(QuizRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tagIds", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "authorId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void update(QuizRequest request, @MappingTarget Quiz quiz);

	default TagDto toTagDto(Long id) {
		return new TagDto(id, null);
	}
}
