package com.ta2khu75.thinkhub.modules.post.api.dto;

import java.util.List;

import com.ta2khu75.thinkhub.modules.quiz.api.dto.QuizResponse;

public class PostDetailResponse extends PostResponse {
	List<QuizResponse> quizzes;
}
