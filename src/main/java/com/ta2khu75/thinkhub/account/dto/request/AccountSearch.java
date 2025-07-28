package com.ta2khu75.thinkhub.account.dto.request;

import com.ta2khu75.thinkhub.shared.dto.DateRange;
import com.ta2khu75.thinkhub.shared.dto.Search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountSearch extends Search {
	private Boolean nonLocked;
	private Boolean enabled;
	private Long roleId;
	private DateRange created;
	private DateRange updated;
}
