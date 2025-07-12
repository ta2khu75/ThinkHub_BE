package com.ta2khu75.thinkhub.post.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import com.ta2khu75.quiz.model.entity.Blog;
import com.ta2khu75.quiz.model.entity.Comment;
import com.ta2khu75.quiz.model.entity.Quiz;
import com.ta2khu75.quiz.model.request.BlogRequest;
import com.ta2khu75.quiz.model.response.BlogResponse;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.model.response.QuizResponse;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AccountMapper.class , IdMapper.class})
public interface BlogMapper extends PageMapper<Blog, BlogResponse> {
	default int mapCommentCount(List<Comment> comments) {
	    return comments != null ? comments.size() : 0;
	}
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "quizzes", ignore = true)
	@Mapping(target = "viewCount", ignore = true)
	@Mapping(target = "tags", ignore = true)
	Blog toEntity(BlogRequest blogResponse);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "quizzes", ignore = true)
	@Mapping(target = "viewCount", ignore = true)
	@Mapping(target = "tags", ignore = true)
	void update(BlogRequest blogResponse, @MappingTarget Blog blog);

	@Named("toQuizResponse")
	@Mapping(target = "id", source = "quiz", qualifiedByName = "encodeId")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "blog", source = "blog", qualifiedByName = "toBlogResponse")
	@Mapping(target = "questions", ignore = true)
	QuizResponse toResponse(Quiz quiz);

	@Named("toBlogResponse")
	@Mapping(target = "id", source = "blog", qualifiedByName = "encodeId")
	@Mapping(target = "commentCount", source = "comments")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "content", ignore = true)
	@Mapping(target = "quizzes", ignore = true)
	BlogResponse toResponse(Blog blog);

	@Named("toBlogDetailsResponse")
	@Mapping(target = "id", source = "blog", qualifiedByName = "encodeId")
	@Mapping(target = "commentCount", source = "comments")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "quizzes", source = "quizzes", qualifiedByName = "toQuizResponse")
	BlogResponse toDetailsResponse(Blog blog);

//	@Mapping(target = "page", source = "number")
//	@Mapping(target = "content", source = "content", qualifiedByName = "toBlogResponse")
//	PageResponse<BlogResponse> toPageResponse(Page<Blog> blogs);
}
