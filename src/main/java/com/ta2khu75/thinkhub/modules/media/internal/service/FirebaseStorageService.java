package com.ta2khu75.thinkhub.modules.media.internal.service;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.ta2khu75.thinkhub.modules.media.internal.domain.MediaOwnerType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseStorageService implements StorageStrategy {
	private final Storage storage;
	@Value("${firebase.storage-bucket}")
	private String storageBucket;

	@Override
	public String upload(MultipartFile file) throws IOException {
		String filePath = this.getFilePath(file);
		Bucket bucket = storage.get(storageBucket);
		Blob blob = bucket.create(filePath, file.getInputStream(), file.getContentType());
		blob.toBuilder().setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))).build()
				.update();
		return String.format("https://storage.googleapis.com/%s/%s", storageBucket, filePath);
	}

	private String getFilePath(MultipartFile file) {
		String fileName = UUID.randomUUID().toString().concat(this.getExtension(file));
		String prefix = fileName.substring(0, 2);
		LocalDate localDate = LocalDate.now();
		return String.format("%d/%d/%s/%s", localDate.getYear(), localDate.getMonthValue(), prefix, fileName);
	}

	public void delete(String url) {
		try {
			String fileName = getFileNameFromUrl(url);
			boolean deleted = storage.delete(storageBucket, fileName);
			if (!deleted) {
				log.warn("File {} not exist in bucket {}", fileName, storageBucket);
			}
		} catch (StorageException e) {
			log.error("Xóa file thất bại: {}", e.getMessage(), e);
			throw e;
		}
	}

	private String getExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private String getFileNameFromUrl(String url) {
		URI uri = URI.create(url);
		String path = uri.getPath(); // Lấy phần đường dẫn của URL
		return path.substring(path.indexOf('/') + 1); // Bỏ dấu "/" ở đầu
	}
}
