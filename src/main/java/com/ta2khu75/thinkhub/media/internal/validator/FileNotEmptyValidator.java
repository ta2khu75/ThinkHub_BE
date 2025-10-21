package com.ta2khu75.thinkhub.media.internal.validator;

import org.springframework.web.multipart.MultipartFile;

import com.ta2khu75.thinkhub.media.internal.anotation.FileNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmpty, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		return file != null && !file.isEmpty();
	}
}
