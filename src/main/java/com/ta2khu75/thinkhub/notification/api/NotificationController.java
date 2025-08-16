package com.ta2khu75.thinkhub.notification.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ta2khu75.thinkhub.notification.api.dto.NotificationIdDto;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationResponse;
import com.ta2khu75.thinkhub.notification.api.dto.NotificationStatusRequest;
import com.ta2khu75.thinkhub.shared.anotation.ApiController;
import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;
import com.ta2khu75.thinkhub.shared.api.controller.BaseController;
import com.ta2khu75.thinkhub.shared.api.dto.PageResponse;
import com.ta2khu75.thinkhub.shared.api.dto.Search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Notification", description = "View, update, and manage user notifications in the system.")
@ApiController("${app.api-prefix}/notifications")
public class NotificationController extends BaseController<NotificationApi> {

	public NotificationController(NotificationApi service) {
		super(service);
	}

	@GetMapping
	@Operation(summary = "Get notifications", description = "Retrieve a paginated list of notifications for the current user.")
	public ResponseEntity<PageResponse<NotificationResponse>> readPage(@SnakeCaseModelAttribute Search search) {
		return ResponseEntity.ok(service.readPage(search));
	}

	@PutMapping

	@Operation(summary = "Update notification status", description = "Mark notifications as read, unread, or perform other status changes.")
	public ResponseEntity<NotificationResponse> update(@RequestBody @Valid NotificationStatusRequest request) {
		return ResponseEntity.ok(service.update(request));
	}

	@DeleteMapping
	@Operation(summary = "Delete a notification", description = "Remove a specific notification from your inbox.")
	public ResponseEntity<Void> delete(@RequestBody NotificationIdDto id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
