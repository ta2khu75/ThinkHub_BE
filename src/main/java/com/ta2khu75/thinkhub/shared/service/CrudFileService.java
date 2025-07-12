package com.ta2khu75.thinkhub.shared.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

public interface CrudFileService <REQ, RES, ID> {
    RES create(@Valid REQ request, MultipartFile file)throws IOException;
    RES update(ID id,@Valid REQ request, MultipartFile file)throws IOException;
    RES read(ID id);
	void delete(ID id);
}