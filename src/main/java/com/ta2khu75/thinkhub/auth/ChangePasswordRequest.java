package com.ta2khu75.thinkhub.auth;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank(message = "Password must not be blank") String password,
		@NotBlank(message = "New password must not be blank") String newPassword,
		@NotBlank(message = "Confirm password must not be blank") String confirmPassword) {

}
