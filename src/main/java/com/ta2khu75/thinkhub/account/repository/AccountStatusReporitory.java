package com.ta2khu75.thinkhub.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ta2khu75.thinkhub.account.entity.AccountStatus;

public interface AccountStatusReporitory extends JpaRepository<AccountStatus, Long> {
}
