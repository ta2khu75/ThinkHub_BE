package com.ta2khu75.thinkhub.shared.exception;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ta2khu75.thinkhub.shared.dto.ApiResponse;

@RestControllerAdvice
public class HandleException implements ResponseBodyAdvice<Object> {
	@ExceptionHandler(value = { NotMatchesException.class, ExistingException.class, InvalidDataException.class,
			MissingRequestCookieException.class })
	public ResponseEntity<ExceptionResponse> handleBadRequest(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ExceptionResponse(ex.getConstraintViolations().iterator().next().getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors
				.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacement) -> existing // Nếu
																												// trùng
																												// field,
																												// giữ
																												// lại
																												// lỗi
																												// đầu
																												// tiên
				));

		ApiResponse response = ApiResponse.builder().status(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.message("Validation failed").error(errors).build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnAuthorizedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(value = { NoResourceFoundException.class, NotFoundException.class })
	public ResponseEntity<ExceptionResponse> handleInValidDataException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(value = { DisabledException.class, BadCredentialsException.class })
	public ResponseEntity<ExceptionResponse> handleDisabledException(Exception ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
//		ex.printStackTrace();
		return ResponseEntity.internalServerError().body(new ExceptionResponse(ex.getMessage()));
	}

	@Override
	public boolean supports(@NonNull MethodParameter returnType,
			@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType,
			@NonNull MediaType selectedContentType,
			@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
			@NonNull ServerHttpResponse response) {
		HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		int status = servletResponse.getStatus();

		if (selectedContentType.includes(MediaType.APPLICATION_JSON)) {
			if (status < HttpStatus.BAD_REQUEST.value()) {
				return ApiResponse.builder().data(body).status(status).build();
			} else if (body instanceof ExceptionResponse exceptionResponse) {
				return ApiResponse.builder().message(exceptionResponse.messageError).status(status).build();
			}
		}
		return body;
	}

	public record ExceptionResponse(String messageError) {
	}

	public record FieldErrorResponse(String field, String message) {
	}

}
