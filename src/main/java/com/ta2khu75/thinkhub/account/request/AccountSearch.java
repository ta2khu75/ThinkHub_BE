package com.ta2khu75.thinkhub.account.request;

import java.time.Instant;

import com.ta2khu75.thinkhub.shared.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountSearch extends Search {
	private Boolean nonLocked;
	private Boolean enabled;
	private Long roleId;
	private Instant createdFrom;
	private Instant createdTo;
}
