package com.ta2khu75.thinkhub.quiz.service;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.quiz.QuizService;
import com.ta2khu75.thinkhub.quiz.dto.QuizRequest;
import com.ta2khu75.thinkhub.quiz.dto.QuizResponse;
import com.ta2khu75.thinkhub.quiz.dto.QuizSearch;
import com.ta2khu75.thinkhub.quiz.entity.Quiz;
import com.ta2khu75.thinkhub.quiz.mapper.QuizMapper;
import com.ta2khu75.thinkhub.quiz.repository.QuizRepository;
import com.ta2khu75.thinkhub.shared.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.service.BaseFileService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class QuizServiceImpl extends BaseFileService<Quiz, Long, QuizRepository, QuizMapper> implements QuizService {
	public QuizServiceImpl(QuizRepository repository, QuizMapper mapper, FirebaseService fireBaseService) {
		super(repository, mapper, fireBaseService);
	}
	

//	private Quiz findById(String id) {
//		Long idDecode = decodeId(id);
//		return repository.findById(idDecode)
//				.orElseThrow(() -> new NotFoundException("Could not found quiz with id: " + idDecode));
//	}

	private QuizResponse save(Quiz quiz) {
		Quiz quizSaved = repository.save(quiz);
//		applicationEventPublisher.publishEvent(new NotificationEvent(this, quizSaved.getId(), TargetType.QUIZ));
		return mapper.toResponse(quizSaved);
	}

	@Override
	@Transactional
	@Validated(value = { Default.class })
	public QuizResponse create(@Valid QuizRequest quizRequest, MultipartFile file) throws IOException {
		Quiz quiz = mapper.toEntity(quizRequest);
		quiz.setAuthor(SecurityUtil.getCurrentProfile());
		fileUtil.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImagePath);
		return this.save(quiz);
//		Quiz quizSaved = repository.save(quiz);
//		applicationEventPublisher.publishEvent(new NotificationEvent(this, quizSaved.getId(), TargetType.QUIZ));
//		return mapper.toResponse(quizSaved);
//		Quiz quiz = mapper.toEntity(quizRequest);
//		fileUtil.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImagePath);
//		quiz.setCategory(this.findExamCategoryById(quizRequest.getCategoryId()));
//		quiz.setAuthor(SecurityUtil.getCurrentProfile());
//		if(quizRequest.getBlogId() != null) {
//			
//		}
//		setBlog(quiz, quizRequest.getBlogId());
//		Quiz quizSaved = repository.save(quiz);
//		quizRequest.getQuestions().forEach(question-> {
//			question.setQuiz(quizSaved);
//			questionService.create(question);
//		});
//		applicationEventPublisher.publishEvent(new NotificationEvent(this, quizSaved.getId(), TargetType.QUIZ));
//		return mapper.toResponse(repository.save(quizSaved));
	}

	private Long decodeId(String quizId) {
		return SqidsUtil.decodeWithSalt(quizId, SaltedType.QUIZ);
	}

	@Override
	@Transactional
	@Validated(value = { Default.class })
	public QuizResponse update(String id, @Valid QuizRequest quizRequest, MultipartFile file) throws IOException {
		Quiz quiz = this.findById(id);

//		Map<Long, QuestionRequest> requestQuestionMap = quizRequest.getQuestions().stream().filter(question -> question.getId() != null)
//				.collect(Collectors.toMap(QuestionRequest::getId, Function.identity()));
//		if (!quiz.isCompleted()) {
//			Iterator<Question> questionIterable = quiz.getQuestions().iterator();
//			while (questionIterable.hasNext()) {
//				Question existingQuestion = questionIterable.next();
//				QuestionRequest questionRequest= requestQuestionMap.get(existingQuestion.getId());
//				if (questionRequest!= null) {
//					questionService.update(questionRequest.getId(), questionRequest);
//				} else {
//					questionIterable.remove();
//					questionService.delete(existingQuestion.getId());
//				}
//			}
//			quizRequest.getQuestions().stream().filter(question -> question.getId() == null).forEach(question-> {
//				question.setQuiz(quiz);
//				questionService.create(question);
//			});
//		}
//		mapper.update(quizRequest, quiz);
//		setBlog(quiz, quizRequest.getBlogId());
		fileUtil.saveFile(quiz, file, Folder.QUIZ_FOLDER, Quiz::setImagePath);
		mapper.update(quizRequest, quiz);
//		if (quiz.getCategory().getId().equals(quizRequest.getCategoryId()))
//			quiz.setCategory(this.findExamCategoryById(quizRequest.getCategoryId()));
		// mapper.toResponse(repository.save(quiz));
		return this.save(quiz);
	}

	@Override
	public QuizResponse read(String id) {
		Quiz quiz = this.findById(id);
		return mapper.toDetailResponse(quiz);
	}

	@Override
	@Transactional
	public void delete(String id) {
		Quiz quiz = this.findById(id);
		if (quiz.isCompleted()) {
			quiz.setDeleted(true);
			repository.save(quiz);
		} else {
			repository.delete(quiz);
		}
	}

	@Override
	public QuizResponse readDetail(String id) {
		Quiz quiz = this.findById(id);
		return mapper.toQuizQuestionDetailResponse(quiz);
	}

	@Override
	public PageResponse<QuizResponse> search(QuizSearch search) {
		if (!SecurityUtil.isAuthor(search.getAuthorId()))
			search.setAccessModifier(AccessModifier.PUBLIC);
		Page<Quiz> page = repository.search(search);
		return mapper.toPageResponse(page);
	}

	@Override
	public List<QuizResponse> readAllByAuthorIdAndKeywork(Long authorId, String keyword) {
		return repository.findByAuthorIdAndTitleContainingIgnoreCaseAndBlogIsNull(authorId, keyword).stream()
				.map(mapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public QuizResponse create(@Valid QuizRequest request, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuizResponse update(String id, @Valid QuizRequest request, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponse<QuizResponse> search(QuizSearch search) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Long countByAuthorEmail(String authorEmail) {
//		return repository.countByAuthorEmail(authorEmail);
//	}
//
//	@Override
//	public Long countByAuthorIdAndAccessModifier(String authorId, AccessModifier accessModifier) {
//		return repository.countByAuthorIdAndAccessModifier(authorId, accessModifier);
//	}
//
//	@Override
//	public List<QuizResponse> myReadAllById(List<String> ids) {
//		return repository.findAllById(ids).stream().map(mapper::toResponse).collect(Collectors.toList());
//	}
//
//	@Override
//	public PageResponse<QuizResponse> mySearchExamNull(String keyword, Pageable pageable) {
//		String accountId = SecurityUtil.getCurrentUserLogin();
//		Page<Quiz> response = repository.findByAuthorIdAndTitleContainingAndBlogIdIsNull(accountId, keyword, pageable);
//		return mapper.toPageResponse(response);
//	}
}
