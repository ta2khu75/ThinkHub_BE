package com.ta2khu75.thinkhub.report.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.quiz.exception.InvalidDataException;
import com.ta2khu75.quiz.mapper.BlogMapper;
import com.ta2khu75.quiz.mapper.QuizMapper;
import com.ta2khu75.quiz.mapper.ReportMapper;
import com.ta2khu75.quiz.model.ReportStatus;
import com.ta2khu75.quiz.model.TargetType;
import com.ta2khu75.quiz.model.entity.AccountProfile;
import com.ta2khu75.quiz.model.entity.Blog;
import com.ta2khu75.quiz.model.entity.Quiz;
import com.ta2khu75.quiz.model.entity.Report;
import com.ta2khu75.quiz.model.entity.id.ReportId;
import com.ta2khu75.quiz.model.request.ReportRequest;
import com.ta2khu75.quiz.model.request.search.ReportSearch;
import com.ta2khu75.quiz.model.request.update.ReportStatusRequest;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.model.response.ReportResponse;
import com.ta2khu75.quiz.repository.BlogRepository;
import com.ta2khu75.quiz.repository.QuizRepository;
import com.ta2khu75.quiz.repository.ReportRepository;
import com.ta2khu75.quiz.repository.account.AccountRepository;
import com.ta2khu75.quiz.service.ReportService;
import com.ta2khu75.quiz.service.base.BaseService;
import com.ta2khu75.quiz.util.Base62;
import com.ta2khu75.quiz.util.FunctionUtil;
import com.ta2khu75.quiz.util.SaltedType;
import com.ta2khu75.quiz.util.SecurityUtil;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl extends BaseService<ReportRepository, ReportMapper> implements ReportService {
	BlogRepository blogRepository;
	BlogMapper blogMapper;
	QuizRepository quizRepository;
	QuizMapper quizMapper;

	public ReportServiceImpl(ReportRepository repository, ReportMapper mapper, AccountRepository accountRepository,
			BlogRepository blogRepository, BlogMapper blogMapper, QuizRepository quizRepository,
			QuizMapper quizMapper) {
		super(repository, mapper);
		this.blogRepository = blogRepository;
		this.blogMapper = blogMapper;
		this.quizRepository = quizRepository;
		this.quizMapper = quizMapper;
	}

	private Object getTarget(Long targetId, TargetType targetType) {
		switch (targetType) {
		case BLOG: {
			return FunctionUtil.findOrThrow(targetId, Blog.class, blogRepository::findById);
		}
		case QUIZ: {
			return FunctionUtil.findOrThrow(targetId, Quiz.class, quizRepository::findById);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + targetType);
		}
	}

	private ReportResponse toResponse(Report report, Object target) {
		ReportResponse response = mapper.toResponse(report,report);
		if (response.getId().targetType().equals(TargetType.BLOG)) {
			response.setTarget(blogMapper.toResponse((Blog) target));
		} else {
			response.setTarget(quizMapper.toResponse((Quiz) target));
		}
		return response;
	}

	private void checkAuthor(Object target, TargetType targetType, Long profileId) {
		Long authorId;
		switch (targetType) {
		case BLOG: {
			authorId = ((Blog) target).getAuthor().getId();
			break;
		}
		case QUIZ: {
			authorId = ((Quiz) target).getAuthor().getId();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + targetType);
		}
		if (authorId.equals(profileId)) {
			throw new InvalidDataException("You can't report your own content");
		}
	}

	private Long decodeTargetId(String targetId, TargetType targetType) {
		switch (targetType) {
		case BLOG: {
			return Base62.decodeWithSalt(targetId, SaltedType.BLOG);
		}
		case QUIZ: {
			return Base62.decodeWithSalt(targetId, SaltedType.QUIZ);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + targetType);
		}
	}

	@Override
	@Transactional
	public ReportResponse create(ReportRequest request) {
		AccountProfile profile= SecurityUtil.getCurrentProfile();
		Long targetId = decodeTargetId(request.getTargetId(), request.getTargetType());
		Object target = getTarget(targetId, request.getTargetType());
		checkAuthor(target, request.getTargetType(), profile.getId());
		Report report = mapper.toEntity(request);
		report.setId(new ReportId(profile.getId(), targetId, request.getTargetType()));
		report.setAuthor(profile);
		report = repository.save(report);
		return toResponse(report, target);
	}
	@Override
	public ReportResponse update(@Valid ReportRequest request) {
		Long profileId= SecurityUtil.getCurrentProfileId();
		Long targetId = decodeTargetId(request.getTargetId(), request.getTargetType());
		Report report = FunctionUtil.findOrThrow(new ReportId(profileId, targetId, request.getTargetType()), Report.class, repository::findById);
		checkAuthor(getTarget(targetId, request.getTargetType()), report.getId().getTargetType(), profileId);
		if (report.getStatus().equals(ReportStatus.PENDING)) {
			mapper.update(request, report);
			report = repository.save(report);
			return mapper.toResponse(report, report);
		} else {
			throw new InvalidDataException("You can't update report with status " + report.getStatus());
		}
	}

	@Override
	public PageResponse<ReportResponse> search(ReportSearch search) {
		Pageable pageable = Pageable.ofSize(search.getSize()).withPage(search.getPage());
		Page<Report> page = repository.search(search.getAuthorId(), search.getTargetType(), search.getReportType(),
				search.getReportStatus(), search.getFromDate(), search.getToDate(), pageable);
		PageResponse<ReportResponse> response = mapper.toPageResponse(page.map(report -> toResponse(report, getTarget(report.getId().getTargetId(), report.getId().getTargetType()))));
		return response;
	}


//	@Override
//	public ReportResponse read(String id) {
//		Long authorId = SecurityUtil.getCurrentProfileId();
//		Report report = FunctionUtil.findOrThrow(new ReportId(authorId, id), Report.class, repository::findById);
//		return mapper.toResponse(report);
//	}
//
	@Override
	public ReportResponse updateStatus(ReportStatusRequest request) {
		ReportId reportId= mapper.toEntity(request.getId(),request.getId());
		Report report= FunctionUtil.findOrThrow(reportId, Report.class, repository::findById);
		report.setStatus(request.getStatus());
		return toResponse(repository.save(report), getTarget(reportId.getTargetId(), report.getId().getTargetType()));
	}
}
