package com.ta2khu75.thinkhub.media.internal;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.media.api.MediaApi;
import com.ta2khu75.thinkhub.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.media.internal.entity.Media;
import com.ta2khu75.thinkhub.media.internal.entity.MediaType;
import com.ta2khu75.thinkhub.media.internal.mapper.MediaMapper;
import com.ta2khu75.thinkhub.media.internal.repository.MediaRepository;
import com.ta2khu75.thinkhub.media.internal.service.StorageStrategy;
import com.ta2khu75.thinkhub.shared.enums.EntityType;
import com.ta2khu75.thinkhub.shared.service.BaseService;

@Service
public class MediaServiceImpl extends BaseService<Media, Long, MediaRepository, MediaMapper> implements MediaApi {
	private final StorageStrategy storageStrategy;

	public MediaServiceImpl(MediaRepository repository, MediaMapper mapper, StorageStrategy storageStrategy) {
		super(repository, mapper);
		this.storageStrategy = storageStrategy;
	}

	@Override
	public MediaResponse create(MediaRequest request) throws IOException {
		Media media = new Media();
		media.setOwner(request.owner());
		media.setOwnerId(request.ownerId());
		this.updateMedia(media, request.file());
		return saveAndResponse(media);
	}

	@Override
	public MediaResponse update(Long id, MediaRequest request) throws IOException {
		Media media = this.readEntity(id);
		String oldUrl = media.getUrl();
		this.updateMedia(media, request.file());
		MediaResponse response = this.saveAndResponse(media);
		storageStrategy.delete(oldUrl);
		return response;
	}

	@Override
	public void delete(Long id) {
		Media media = this.readEntity(id);
		storageStrategy.delete(media.getUrl());
		repository.deleteById(id);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.MEDIA;
	}

	private void updateMedia(Media media, MultipartFile file) throws IOException {
		Long size = file.getSize();
		String filename = file.getOriginalFilename();
		String url = storageStrategy.upload(media.getOwner(), file);
		MediaType type = MediaType.fromMimeType(file.getContentType());
		media.setUrl(url);
		media.setSize(size);
		media.setType(type);
		media.setFilename(filename);
	}

	private MediaResponse saveAndResponse(Media source) {
		Media media = repository.save(source);
		return mapper.convert(media);
	}

	@Override
	public MediaResponse read(Long id) {
		return mapper.convert(readEntity(id));
	}

}
