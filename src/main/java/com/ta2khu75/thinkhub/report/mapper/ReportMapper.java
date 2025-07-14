package com.ta2khu75.thinkhub.report.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.ta2khu75.quiz.model.dto.ReportIdDto;
import com.ta2khu75.quiz.model.entity.Report;
import com.ta2khu75.quiz.model.entity.base.SaltedIdentifiable;
import com.ta2khu75.quiz.model.entity.id.ReportId;
import com.ta2khu75.quiz.model.request.ReportRequest;
import com.ta2khu75.quiz.model.response.PageResponse;
import com.ta2khu75.quiz.model.response.ReportResponse;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, IdMapper.class})
public interface ReportMapper  {
	@Mapping(target = "targetId", source = "targetId", qualifiedByName = "decode")
	ReportId toEntity(ReportIdDto dto, @Context SaltedIdentifiable salted);
	
	@Mapping(target="id", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Report toEntity(ReportRequest request);
	
	@Mapping(target = "target", ignore = true)
	@Mapping(target="id.targetId", source = "id.targetId", qualifiedByName = "encode")
	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
	ReportResponse toResponse(Report entity, @Context SaltedIdentifiable salted);
	
	@Mapping(target="id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target="author", ignore = true)
	void update(ReportRequest request, @MappingTarget Report entity);

	@Mapping(target = "page", source = "number")
	PageResponse<ReportResponse> toPageResponse(Page<ReportResponse> page);
}
