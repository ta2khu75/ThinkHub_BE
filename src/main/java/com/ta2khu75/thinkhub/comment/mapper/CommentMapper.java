package com.ta2khu75.thinkhub.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.ta2khu75.quiz.model.entity.Comment;
import com.ta2khu75.quiz.model.request.CommentRequest;
import com.ta2khu75.quiz.model.response.CommentResponse;
import com.ta2khu75.quiz.model.response.PageResponse;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AccountMapper.class, BlogMapper.class})
public interface CommentMapper extends PageMapper<Comment, CommentResponse>{
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "blog", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Comment toEntity(CommentRequest request);


	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	CommentResponse toResponse(Comment comment);

//	@Mapping(target = "page", source = "number")
//	PageResponse<CommentResponse> toPageResponse(Page<Comment> commentPage);
}
