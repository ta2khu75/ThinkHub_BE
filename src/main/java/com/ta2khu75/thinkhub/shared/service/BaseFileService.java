package com.ta2khu75.thinkhub.shared.service;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService;
import com.ta2khu75.thinkhub.shared.service.clazz.FirebaseService.Folder;

public abstract class BaseFileService<T, ID, R extends JpaRepository<T, ID>, M> extends BaseService<T, ID, R, M> {
	protected final FirebaseService fireBaseService;

	protected BaseFileService(R repository, M mapper, FirebaseService fireBaseService) {
		super(repository, mapper);
		this.fireBaseService = fireBaseService;
	}

	public void saveFile(T entity, MultipartFile file, Folder folder, BiConsumer<T, String> setFilePathFunction)
			throws IOException {
		if (file != null && !file.isEmpty()) {
			String fileUrl = fireBaseService.upload(folder, file);
			setFilePathFunction.accept(entity, fileUrl);
		}
	}
}
