package com.ta2khu75.thinkhub.modules.media.internal;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.modules.media.api.MediaApi;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaRequest;
import com.ta2khu75.thinkhub.modules.media.api.dto.MediaResponse;
import com.ta2khu75.thinkhub.modules.media.internal.domain.Media;
import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaType;
import com.ta2khu75.thinkhub.modules.media.internal.mapper.MediaMapper;
import com.ta2khu75.thinkhub.modules.media.internal.repository.MediaRepository;
import com.ta2khu75.thinkhub.modules.media.internal.service.StorageStrategy;
import com.ta2khu75.thinkhub.modules.media.internal.validator.MediaValidator;
import com.ta2khu75.thinkhub.shared.domain.enums.EntityType;
import com.ta2khu75.thinkhub.shared.service.BaseService;

@Service
class MediaService extends BaseService<Media, Long, MediaRepository> implements MediaApi {
	private final StorageStrategy storageStrategy;

	public MediaService(MediaRepository repository, MediaMapper mapper, MediaValidator validator,
			StorageStrategy storageStrategy) {
		super(repository);
		this.mapper = mapper;
		this.validator = validator;
		this.storageStrategy = storageStrategy;
	}

	private final MediaValidator validator;
	private final MediaMapper mapper;

	private UploadedFileInfo uploadFile(MultipartFile file) throws IOException {
		String url = storageStrategy.upload(file);
		MediaType type = MediaType.fromMimeType(file.getContentType());
		return new UploadedFileInfo(file.getOriginalFilename(), url, file.getSize(), type);
	}

	@Override
	public MediaResponse create(MediaRequest request) throws IOException {
		UploadedFileInfo info = uploadFile(request.file());
		Media media = Media.create(info.filename(), info.url(), info.size(), info.type());
		media = repository.save(media);
		return mapper.convert(media);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.MEDIA;
	}

	@Override
	public MediaResponse read(Long id) {
		return mapper.convert(readEntity(id));
	}

	@Override
	public void ensureExists(Long id) {
		this.assertExists(id);
	}

	@Override
	public void attach(Long id) {
		Media media = this.readEntity(id);
		media.attach();
		repository.save(media);
	}

	@Override
	public void delete(Long id) {
		Media media = this.readEntity(id);
		media.markDeleted();
		repository.save(media);
	}

	@Override
	public void detach(Long id) {
		Media media = this.readEntity(id);
		media.detach();
		repository.save(media);
	}

	@Override
	public String readUrl(Long id) {
		String url = repository.findUrlById(id).orElse(null);
		validator.validateUrlExists(id, url);
		return url;
	}

}

record UploadedFileInfo(String filename, String url, Long size, MediaType type) {
}