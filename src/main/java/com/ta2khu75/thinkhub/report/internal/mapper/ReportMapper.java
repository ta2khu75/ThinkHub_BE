package com.ta2khu75.thinkhub.report.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.report.api.dto.ReportRequest;
import com.ta2khu75.thinkhub.report.api.dto.ReportResponse;
import com.ta2khu75.thinkhub.report.internal.entity.Report;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;

@Mapper(config = MapperSpringConfig.class)
public interface ReportMapper extends Converter<Report, ReportResponse>, PageMapper<Report, ReportResponse> {
	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "target", ignore = true)
	ReportResponse convert(Report source);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Report toEntity(ReportRequest request);
//
//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "author", ignore = true)
//	@Mapping(target = "status", ignore = true)
//	@Mapping(target = "createdAt", ignore = true)
//	@Mapping(target = "updatedAt", ignore = true)
//	Report toEntity(ReportRequest request);
//
//	@Mapping(target = "target", ignore = true)
//	@Mapping(target = "id.targetId", source = "id.targetId", qualifiedByName = "encode")
//	@Mapping(target = "author", source = "author", qualifiedByName = "toProfileResponse")
//	ReportResponse toResponse(Report entity, @Context SaltedIdentifiable salted);
//
//	@Mapping(target = "id", ignore = true)
//	@Mapping(target = "status", ignore = true)
//	@Mapping(target = "createdAt", ignore = true)
//	@Mapping(target = "updatedAt", ignore = true)
//	@Mapping(target = "author", ignore = true)
//	void update(ReportRequest request, @MappingTarget Report entity);
//
//	@Mapping(target = "page", source = "number")
//	PageResponse<ReportResponse> toPageResponse(Page<ReportResponse> page);
}
