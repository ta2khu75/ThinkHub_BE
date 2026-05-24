package com.ta2khu75.thinkhub.modules.report.internal;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.report.api.ReportApi;
import com.ta2khu75.thinkhub.modules.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.modules.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.modules.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.modules.report.api.event.ReportCreatedEvent;
import com.ta2khu75.thinkhub.modules.report.internal.domain.Report;
import com.ta2khu75.thinkhub.modules.report.internal.domain.ReportStatus;
import com.ta2khu75.thinkhub.modules.report.internal.mapper.ReportMapper;
import com.ta2khu75.thinkhub.modules.report.internal.repository.ReportRepository;
import com.ta2khu75.thinkhub.modules.report.internal.validator.ReportValidator;
import com.ta2khu75.thinkhub.modules.report.required.port.ReportUserPort;
import com.ta2khu75.thinkhub.shared.common.api.dto.AuthorResponse;
import com.ta2khu75.thinkhub.shared.common.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ReportService extends BaseService<Report, Long, ReportRepository> implements ReportApi {

	public ReportService(ReportRepository repository, ReportMapper mapper, ReportUserPort userPort,
			ReportValidator validator, ApplicationEventPublisher events) {
		super(repository);
		this.events = events;
		this.mapper = mapper;
		this.userPort= userPort;
		this.validator = validator;
	}

	private final ReportUserPort userPort;
	private final ReportMapper mapper;
	private final ReportValidator validator;
	private final ApplicationEventPublisher events;

	@Override
	public ReportResponse create(ReportRequest request) {
		Long authorId = SecurityUtil.getCurrentUserIdDecode();
		Report report = mapper.toEntity(request);
		report.setTargetId(String.valueOf(this.convertTargetId(request)));
		report.setAuthorId(authorId);
		report = repository.save(report);
		events.publishEvent(new ReportCreatedEvent(report.getId()));
		return toResponse(report);
	}

	@Override
	public ReportResponse update(Long id, @Valid ReportRequest request) {
		Report report = this.readEntity(id);
		validator.validateUpdate(report);
		report.setType(request.type());
		report.setType(request.type());
		repository.save(report);
		return toResponse(report);
	}

	@Override
	public ReportResponse read(Long id) {
		Report report = readEntity(id);
		return toResponse(report);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public ReportResponse updateStatus(Long id, ReportStatus status) {
		Report report = this.readEntity(id);
		report.setStatus(status);
		report = repository.save(report);
		return toResponse(report);
	}

	private Long convertTargetId(ReportRequest request) {
		String targetId = request.targetId();
		switch (request.targetType()) {
		case POST:
			return decode(targetId, IdConfig.POST);
		case QUIZ:
			return decode(targetId, IdConfig.QUIZ);
		case COMMENT:
			return Long.valueOf(targetId);
		default:
			throw new IllegalArgumentException("Unexpected value: " + request.targetType());
		}
	}

	private ReportResponse toResponse(Report report) {
		AuthorResponse author = userPort.readAuthor(report.getAuthorId());
		ReportResponse response = mapper.convert(report);
		response.setAuthor(author);
		return response;
	}

	@Override
	public PageResponse<ReportResponse> search(ReportSearch search) {
		return null;
	}

//	BlogRepository blogRepository;
//	BlogMapper blogMapper;
//	QuizRepository quizRepository;
//	QuizMapper quizMapper;
//
//	public ReportServiceImpl(ReportRepository repository, ReportMapper mapper, AccountRepository accountRepository,
//			BlogRepository blogRepository, BlogMapper blogMapper, QuizRepository quizRepository,
//			QuizMapper quizMapper) {
//		super(repository, mapper);
//		this.blogRepository = blogRepository;
//		this.blogMapper = blogMapper;
//		this.quizRepository = quizRepository;
//		this.quizMapper = quizMapper;
//	}
//
//	private Object getTarget(Long targetId, CommentTarget targetType) {
//		switch (targetType) {
//		case BLOG: {
//			return FunctionUtil.findOrThrow(targetId, Blog.class, blogRepository::findById);
//		}
//		case QUIZ: {
//			return FunctionUtil.findOrThrow(targetId, Quiz.class, quizRepository::findById);
//		}
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + targetType);
//		}
//	}
//
//	private ReportResponse toResponse(Report report, Object target) {
//		ReportResponse response = mapper.toResponse(report,report);
//		if (response.getId().targetType().equals(CommentTarget.BLOG)) {
//			response.setTarget(blogMapper.toResponse((Blog) target));
//		} else {
//			response.setTarget(quizMapper.toResponse((Quiz) target));
//		}
//		return response;
//	}
//
//	private void checkAuthor(Object target, CommentTarget targetType, Long profileId) {
//		Long authorId;
//		switch (targetType) {
//		case BLOG: {
//			authorId = ((Blog) target).getAuthor().getId();
//			break;
//		}
//		case QUIZ: {
//			authorId = ((Quiz) target).getAuthor().getId();
//			break;
//		}
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + targetType);
//		}
//		if (authorId.equals(profileId)) {
//			throw new InvalidDataException("You can't report your own content");
//		}
//	}
//
//	private Long decodeTargetId(String targetId, CommentTarget targetType) {
//		switch (targetType) {
//		case BLOG: {
//			return Base62.decodeWithSalt(targetId, SaltedType.BLOG);
//		}
//		case QUIZ: {
//			return Base62.decodeWithSalt(targetId, SaltedType.QUIZ);
//		}
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + targetType);
//		}
//	}
//
//	@Override
//	@Transactional
//	public ReportResponse create(ReportRequest request) {
//		AccountProfile profile= SecurityUtil.getCurrentProfile();
//		Long targetId = decodeTargetId(request.getTargetId(), request.getTargetType());
//		Object target = getTarget(targetId, request.getTargetType());
//		checkAuthor(target, request.getTargetType(), profile.getId());
//		Report report = mapper.toEntity(request);
//		report.setId(new ReportId(profile.getId(), targetId, request.getTargetType()));
//		report.setAuthor(profile);
//		report = repository.save(report);
//		return toResponse(report, target);
//	}
//	@Override
//	public ReportResponse update(@Valid ReportRequest request) {
//		Long profileId= SecurityUtil.getCurrentProfileId();
//		Long targetId = decodeTargetId(request.getTargetId(), request.getTargetType());
//		Report report = FunctionUtil.findOrThrow(new ReportId(profileId, targetId, request.getTargetType()), Report.class, repository::findById);
//		checkAuthor(getTarget(targetId, request.getTargetType()), report.getId().getTargetType(), profileId);
//		if (report.getStatus().equals(ReportStatus.PENDING)) {
//			mapper.update(request, report);
//			report = repository.save(report);
//			return mapper.toResponse(report, report);
//		} else {
//			throw new InvalidDataException("You can't update report with status " + report.getStatus());
//		}
//	}
//
//	@Override
//	public PageResponse<ReportResponse> search(ReportSearch search) {
//		Pageable pageable = Pageable.ofSize(search.getSize()).withPage(search.getPage());
//		Page<Report> page = repository.search(search.getAuthorId(), search.getTargetType(), search.getReportType(),
//				search.getReportStatus(), search.getFromDate(), search.getToDate(), pageable);
//		PageResponse<ReportResponse> response = mapper.toPageResponse(page.map(report -> toResponse(report, getTarget(report.getId().getTargetId(), report.getId().getTargetType()))));
//		return response;
//	}
//
//
////	@Override
////	public ReportResponse read(String id) {
////		Long authorId = SecurityUtil.getCurrentProfileId();
////		Report report = FunctionUtil.findOrThrow(new ReportId(authorId, id), Report.class, repository::findById);
////		return mapper.toResponse(report);
////	}
////
//	@Override
//	public ReportResponse updateStatus(ReportStatusRequest request) {
//		ReportId reportId= mapper.toEntity(request.getId(),request.getId());
//		Report report= FunctionUtil.findOrThrow(reportId, Report.class, repository::findById);
//		report.setStatus(request.getStatus());
//		return toResponse(repository.save(report), getTarget(reportId.getTargetId(), report.getId().getTargetType()));
//	}

}
