package com.ta2khu75.thinkhub.user.api.dto;

import java.time.LocalDate;

public record UserDto(String id, String email, String firstName, String lastName, LocalDate birthday, String username, String summary,
		UserStatusResponse status) {

}
