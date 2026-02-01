package com.ta2khu75.thinkhub.modules.user.api.dto;

import java.time.LocalDate;

import com.ta2khu75.thinkhub.shared.common.api.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSearch extends Search {
	private Boolean nonLocked;
	private Boolean enabled;
	private Long roleId;
	private LocalDate createdFrom;
	private LocalDate createdTo;
	private LocalDate updatedFrom;
	private LocalDate updatedTo;
}
