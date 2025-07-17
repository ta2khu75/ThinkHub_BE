package com.ta2khu75.thinkhub.comment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.comment.dto.CommentRequest;
import com.ta2khu75.thinkhub.comment.dto.CommentResponse;
import com.ta2khu75.thinkhub.comment.entity.Comment;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface CommentMapper extends Converter<Comment, CommentResponse> {
	@Override
	@Mapping(target = "author", ignore = true)
	CommentResponse convert(Comment comment);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "content", source = "content")
	Comment toEntity(CommentRequest request);
}
