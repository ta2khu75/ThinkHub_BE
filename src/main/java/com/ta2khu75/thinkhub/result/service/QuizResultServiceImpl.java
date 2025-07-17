package com.ta2khu75.thinkhub.result.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.AnswerDto;
import com.ta2khu75.thinkhub.quiz.dto.QuestionDto;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.result.QuizResultService;
import com.ta2khu75.thinkhub.result.dto.QuizResultRequest;
import com.ta2khu75.thinkhub.result.dto.QuizResultResponse;
import com.ta2khu75.thinkhub.result.dto.QuizResultSearch;
import com.ta2khu75.thinkhub.result.dto.UserAnswerRequest;
import com.ta2khu75.thinkhub.result.entity.QuizResult;
import com.ta2khu75.thinkhub.result.entity.UserAnswer;
import com.ta2khu75.thinkhub.result.mapper.QuizResultMapper;
import com.ta2khu75.thinkhub.result.repository.QuizResultRepository;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService;
import com.ta2khu75.thinkhub.shared.service.clazz.RedisService.RedisKeyBuilder;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;

@Service
public class QuizResultServiceImpl extends BaseService<QuizResult, Long, QuizResultRepository, QuizResultMapper>
		implements QuizResultService {
	private final QuizService quizService;
	private final RedisService redisService;

	public QuizResultServiceImpl(QuizResultRepository repository, QuizResultMapper mapper, QuizService quizService,
			RedisService redisService) {
		super(repository, mapper);
		this.quizService = quizService;
		this.redisService = redisService;
	}

	@Override
	public PageResponse<QuizResultResponse> search(QuizResultSearch search) {
		return null;
	}

	@Override
	public QuizResultResponse take(Long quizId) {
		QuizResultResponse response = redisService.getValue(
				RedisKeyBuilder.quizResult(SecurityUtil.getCurrentAccountIdDecode(), quizId), QuizResultResponse.class);
		if (response == null) {
			QuizResponse quiz = redisService.getValue(RedisKeyBuilder.quiz(quizId), QuizResponse.class);
			if (quiz == null)
				quiz = quizService.readDetail(quizId);
			redisService.setValue(RedisKeyBuilder.quiz(quizId), quiz,
					Instant.now().plusSeconds((quiz.getDuration() + 1) * 60L));
			quiz.getQuestions().forEach(question -> question.answers().forEach(answer -> answer.setCorrect(false)));
			QuizResult quizResult = new QuizResult();
			quizResult.setQuizId(quizId);
			quizResult.setEndTime(Instant.now().plusSeconds(quiz.getDuration() * 60L).plusSeconds(30));
			quizResult.setAccountId(SecurityUtil.getCurrentAccountIdDecode());
			response = mapper.convert(repository.save(quizResult));
			response.setQuiz(quiz);
			if (quiz.isShuffleQuestion()) {
				QuizResponse quizResponse = response.getQuiz();
				List<QuestionDto> questions = new ArrayList<>(quizResponse.getQuestions());
				questions.stream().forEach(question -> {
					if (question.shuffleAnswer()) {
						List<AnswerDto> answers = question.answers();
						Collections.shuffle(answers);
					}
				});
				Collections.shuffle(questions);
				quizResponse.setQuestions(questions);
			}
			redisService.setValue(RedisKeyBuilder.quizResult(SecurityUtil.getCurrentAccountIdDecode(), quizId),
					response, response.getEndTime());
		}
		return response;
	}

	private void gradeQuizResult(QuizResult quizResult, Set<UserAnswerRequest> userAnswerRequests) {
		float totalScore = 0;
		QuizResponse quiz = redisService.getValue(RedisKeyBuilder.quiz(quizResult.getQuizId()), QuizResponse.class);
		List<QuestionDto> questions = quiz.getQuestions();
		Set<UserAnswer> userAnswers = new HashSet<>();
		Map<Long, QuestionDto> questionMap = questions.stream()
				.collect(Collectors.toMap(QuestionDto::id, Function.identity()));
		Map<Long, UserAnswerRequest> userAnswerMap = userAnswerRequests.stream()
				.collect(Collectors.toMap(UserAnswerRequest::questionId, Function.identity()));
		for (Map.Entry<Long, UserAnswerRequest> entry : userAnswerMap.entrySet()) {
			Long questionId = entry.getKey();
			UserAnswerRequest userAnswerRequest = entry.getValue();
			Set<Long> userAnswerIds = userAnswerRequest.answerIds();
			QuestionDto question = questionMap.get(questionId);
			if (question == null)
				continue;
			boolean isCorrect;
			switch (question.type()) {
			case SINGLE_CHOICE:
				isCorrect = isSingleChoiceCorrect(question.answers(), userAnswerIds);
				break;
			case MULTIPLE_CHOICE:
				isCorrect = isMultiChoiceCorrect(question.answers(), userAnswerIds);
				break;
			default:
				isCorrect = false;
			}
			UserAnswer userAnswer = mapper.toEntity(userAnswerRequest);
			userAnswer.setCorrect(isCorrect);
			userAnswers.add(userAnswer);
			totalScore += isCorrect ? 1 : 0;
		}

		// Tính điểm cho một lần
		float averageScore = totalScore / questions.size();
		quizResult.setCorrectCount((int) totalScore);
		quizResult.setScore((float) Math.round(averageScore * 10)); // Đã thay đổi để đơn giản hóa
		quizResult.setUserAnswers(userAnswers);
	}

	@Override
	public QuizResultResponse submit(Long id, QuizResultRequest request) {
		QuizResult quizResult = readEntity(id);
		if (quizResult.getUpdatedAt() != null)
			return mapper.convert(quizResult);
		if (!request.userAnswers().isEmpty()) {
			this.gradeQuizResult(quizResult, request.userAnswers());
		} else {
			quizResult.setCorrectCount(0);
			quizResult.setScore(0f);
		}
		return mapper.convert(repository.save(quizResult));
	}

	@Override
	public QuizResultResponse readDetail(Long id) {
		QuizResult quizResult = readEntity(id);
		Long quizId = quizResult.getQuizId();
		QuizResultResponse response = mapper.convert(quizResult);
		QuizResponse quiz = redisService.getValue(RedisKeyBuilder.quiz(quizId), QuizResponse.class);
		if (quiz == null)
			quiz = quizService.readDetail(quizId);
		response.setQuiz(quiz);
		return response;
	}

	private boolean isSingleChoiceCorrect(List<AnswerDto> answers, Set<Long> answerIds) {
		Long answerId = answerIds.iterator().next();
		return answers.stream().filter(answer -> answer.getId().equals(answerId) && answer.isCorrect()).findFirst()
				.map(answer -> true).orElse(false);
	}

	private boolean isMultiChoiceCorrect(List<AnswerDto> answers, Set<Long> answerIds) {
		Set<Long> correctAnswers = answers.stream().filter(AnswerDto::isCorrect).map(AnswerDto::getId)
				.collect(Collectors.toSet());

		long correctSelected = answerIds.stream().filter(correctAnswers::contains).count();
		long incorrectSelected = answerIds.stream().filter(answerId -> !correctAnswers.contains(answerId)).count();
		return correctSelected == correctAnswers.size() && incorrectSelected == 0;
	}

}
