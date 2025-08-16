package com.ta2khu75.thinkhub.account.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record AccountStatusRequest(@NotNull(message = "Enabled must not be null") Boolean enabled,
		@NotNull(message = "Non locked must not be null") Boolean nonLocked,
		@NotNull(message = "Role id must not be null") Long roleId) {
}
