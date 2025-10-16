package com.ta2khu75.thinkhub.post.internal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import com.ta2khu75.thinkhub.post.api.dto.PostRequest;
import com.ta2khu75.thinkhub.post.api.dto.PostResponse;
import com.ta2khu75.thinkhub.post.internal.entity.Post;
import com.ta2khu75.thinkhub.shared.anotation.MapperSpringConfig;
import com.ta2khu75.thinkhub.shared.mapper.BaseMapper;
import com.ta2khu75.thinkhub.shared.mapper.PageMapper;
import com.ta2khu75.thinkhub.tag.api.dto.TagDto;

@Mapper(config = MapperSpringConfig.class)
public interface PostMapper
		extends Converter<Post, PostResponse>, BaseMapper<PostRequest, Post>, PageMapper<Post, PostResponse> {
	@Override

	@Mapping(target = "id", source = "source")
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "tags", source = "tagIds")
	PostResponse convert(Post source);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "viewCount", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "tagIds", ignore = true)
	@Mapping(target = "quizIds", ignore = true)
	@Mapping(target = "authorId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Post toEntity(PostRequest request);

	@Override
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "imageUrl", ignore = true)
	@Mapping(target = "viewCount", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "tagIds", ignore = true)
	@Mapping(target = "authorId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void update(PostRequest request, @MappingTarget Post entity);

	default TagDto toTagDto(Long id) {
		return new TagDto(id, null);
	}
}
