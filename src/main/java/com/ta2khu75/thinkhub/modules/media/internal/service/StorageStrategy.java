package com.ta2khu75.thinkhub.modules.media.internal.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaOwnerType;

public interface StorageStrategy {
	String upload(MultipartFile file) throws IOException;

	void delete(String url);
}
