package com.ta2khu75.thinkhub.report.internal;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.report.api.ReportApi;
import com.ta2khu75.thinkhub.report.api.dto.ReportIdDto;
import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.api.dto.ReportSearch;
import com.ta2khu75.thinkhub.report.api.dto.ReportStatusRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportUpdateRequest;
import com.ta2khu75.thinkhub.report.internal.entity.Report;
import com.ta2khu75.thinkhub.report.internal.entity.ReportId;
import com.ta2khu75.thinkhub.report.internal.entity.ReportStatus;
import com.ta2khu75.thinkhub.report.internal.enums.ReportTargetType;
import com.ta2khu75.thinkhub.report.internal.mapper.ReportMapper;
import com.ta2khu75.thinkhub.report.internal.repository.ReportRepository;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.entity.AuthorResponse;
import com.ta2khu75.thinkhub.shared.enums.IdConfig;
import com.ta2khu75.thinkhub.shared.exception.InvalidDataException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.encode;
import static com.ta2khu75.thinkhub.shared.util.IdConverterUtil.decode;
import com.ta2khu75.thinkhub.shared.util.SecurityUtil;
import com.ta2khu75.thinkhub.user.api.UserApi;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportServiceImpl extends BaseService<Report, ReportId, ReportRepository, ReportMapper>
		implements ReportApi {
	private final UserApi accountService;

	public ReportServiceImpl(ReportRepository repository, ReportMapper mapper, UserApi accountService) {
		super(repository, mapper);
		this.accountService = accountService;
	}

	@Override
	public ReportResponse read(String targetId, ReportTargetType targetType) {
		ReportId id = toReportId(new ReportIdDto(SecurityUtil.getCurrentUserId(), targetId, targetType));
		Report report = readEntity(id);
		return toResponse(report);
	}

	public ReportResponse create(Long targetId, ReportTargetType targetType, ReportRequest request) {
		Long authorId = SecurityUtil.getCurrentUserIdDecode();
		ReportId id = new ReportId(authorId, targetId, targetType);
		Report report = mapper.toEntity(request);
		report.setId(id);
		report = repository.save(report);
		return toResponse(report);
	}

	private ReportResponse toResponse(Report report) {
		AuthorResponse author = accountService.readAuthor(report.getId().getAuthorId());
		ReportIdDto id = toReportIdDto(report.getId());
		ReportResponse response = mapper.convert(report);
		response.setId(id);
		response.setAuthor(author);
		return response;

	}

	@Override
	public ReportResponse update(ReportUpdateRequest request) {
		Long authorId = SecurityUtil.getCurrentUserIdDecode();
		ReportId id = toReportId(request.id());
		if (!id.getAuthorId().equals(authorId)) {
			throw new InvalidDataException("You can only update your own report");
		}
		Report report = readEntity(id);
		if (report.getStatus().equals(ReportStatus.PENDING)) {
			report.setType(request.type());
			repository.save(report);
			return toResponse(report);
		}
		throw new InvalidDataException("You can't update report with status " + report.getStatus());
	}

	@Override
	public void delete(ReportIdDto dto) {
		ReportId id = toReportId(dto);
		repository.deleteById(id);
	}

	public PageResponse<ReportResponse> search(ReportSearch search) {
		return null;
	}

	@Override
	public ReportResponse updateStatus(ReportStatusRequest request) {
		ReportId id = toReportId(request.id());
		Report report = this.readEntity(id);
		report.setStatus(request.status());
		report = repository.save(report);
		return toResponse(report);
	}

	private ReportIdDto toReportIdDto(ReportId id) {
		String authorId = encode(id.getAuthorId(), IdConfig.USER);
		switch (id.getTargetType()) {
		case POST:
			return new ReportIdDto(authorId, encode(id.getTargetId(), IdConfig.POST), id.getTargetType());
		case QUIZ:
			return new ReportIdDto(authorId, encode(id.getTargetId(), IdConfig.QUIZ), id.getTargetType());
		case COMMENT:
			return new ReportIdDto(authorId, id.getTargetId().toString(), id.getTargetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + id.getTargetType());
		}
	}

	private ReportId toReportId(ReportIdDto dto) {
		Long authorId = decode(dto.authorId(), IdConfig.USER);
		switch (dto.targetType()) {
		case POST:
			return new ReportId(authorId, decode(dto.targetId(), IdConfig.POST), dto.targetType());
		case QUIZ:
			return new ReportId(authorId, decode(dto.targetId(), IdConfig.QUIZ), dto.targetType());
		case COMMENT:
			return new ReportId(authorId, Long.valueOf(dto.targetId()), dto.targetType());
		default:
			throw new IllegalArgumentException("Unexpected value: " + dto.targetType());
		}
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
