package com.ta2khu75.thinkhub.post.draft.internal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftCreateRequest;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftResponse;
import com.ta2khu75.thinkhub.post.draft.api.dto.PostDraftUpdateRequest;
import com.ta2khu75.thinkhub.post.draft.internal.domain.PostDraft;
import com.ta2khu75.thinkhub.shared.common.anotation.MapperSpringConfig;

@Mapper(config = MapperSpringConfig.class)
public interface PostDraftMapper extends Converter<PostDraft, PostDraftResponse> {
	@Override
	@Mapping(target = "imageUrl", ignore = true)
	PostDraftResponse convert(PostDraft source);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "title", source = "title")
	@Mapping(target = "categoryId", source = "categoryId")
	PostDraft toDomain(PostDraftCreateRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ownerId", ignore = true)
	@Mapping(target = "lastModifiedAt", ignore = true)
	void update(PostDraftUpdateRequest request, @MappingTarget PostDraft post);
}
