package com.ta2khu75.thinkhub.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;
import com.ta2khu75.thinkhub.shared.mapper.IdMapper;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { QuestionMapper.class, IdMapper.class })
public interface QuizMapper extends PageMapper<Quiz, QuizResponse> {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "imagePath", ignore = true)
	@Mapping(target = "results", ignore = true)
	@Mapping(target = "blog", source = "blogId")
	@Mapping(target = "category", source = "categoryId")
	@Mapping(target = "questions", source = "questions", qualifiedByName = "toQuestion")
	Quiz toEntity(QuizRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "imagePath", ignore = true)
	@Mapping(target = "results", ignore = true)
	@Mapping(target = "blog", source = "blogId")
	@Mapping(target = "category", source = "categoryId")
	@Mapping(target = "questions", source = "questions", qualifiedByName = "toQuestion")
	void update(QuizRequest request, @MappingTarget Quiz quiz);

	@Named("toQuizResponse")
	@Mapping(target = "id", source = "quiz", qualifiedByName = "encodeId")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "blog", source = "blog", qualifiedByName = "toBlogResponse")
	@Mapping(target = "questions", ignore = true)
	QuizResponse toResponse(Quiz quiz);

	@Named("toQuizDetailResponse")
	@Mapping(target = "id", source = "quiz", qualifiedByName = "encodeId")
	@Mapping(target = "questions", source = "questions", qualifiedByName = "toQuestionDetailDto")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "blog", source = "blog", qualifiedByName = "toBlogResponse")
	QuizResponse toDetailResponse(Quiz quiz);

	@Named("toQuizQuestionDetailResponse")
	@Mapping(target = "id", source = "quiz", qualifiedByName = "encodeId")
	@Mapping(target = "questions", source = "questions", qualifiedByName = "toQuestionAnswerDetailDto")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	@Mapping(target = "blog", source = "blog", qualifiedByName = "toBlogResponse")
	QuizResponse toQuizQuestionDetailResponse(Quiz quiz);

//	@Mapping(target = "page", source = "number")
//	@Mapping(target = "content", source = "content", qualifiedByName = "toQuizResponse")
//	PageResponse<QuizResponse> toPageResponse(Page<Quiz> page);
}
