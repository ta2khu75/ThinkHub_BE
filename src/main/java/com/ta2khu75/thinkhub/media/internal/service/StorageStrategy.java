package com.ta2khu75.thinkhub.media.internal.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.media.internal.entity.MediaOwnerType;

public interface StorageStrategy {
	String upload(MediaOwnerType ownerType, MultipartFile file) throws IOException;

	void delete(String url);
}
