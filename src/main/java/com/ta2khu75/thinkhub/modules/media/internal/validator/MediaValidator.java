package com.ta2khu75.thinkhub.modules.media.internal.validator;

import org.springframework.stereotype.Component;

import com.ta2khu75.thinkhub.modules.media.internal.domain.Media;
import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaStatus;
import com.ta2khu75.thinkhub.shared.exception.BaseValidator;

@Component
public class MediaValidator extends BaseValidator {
	public void validateAttach(Media media) {
		ensure(media.getStatus() == MediaStatus.DRAFT, MediaErrorCode.INVALID_STATE,
				"Media must be in DRAFT state to attach");
	}

	public void validateDetach(Media media) {
		ensure(media.getStatus() == MediaStatus.ATTACHED, MediaErrorCode.INVALID_STATE,
				"Media must be in ATTACHED state to detach");
	}

	public void validateDelete(Media media) {
		ensure(media.getStatus() == MediaStatus.DRAFT, MediaErrorCode.INVALID_STATE,
				"Media cannot be deleted in current state");
	}

	public void validateUrlExists(Long id, String url) {
		notFound(url, MediaErrorCode.NOT_FOUND, "Media not found with id " + id);
	}
}
