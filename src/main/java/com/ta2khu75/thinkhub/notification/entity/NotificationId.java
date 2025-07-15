package com.ta2khu75.thinkhub.notification.entity;

import java.io.Serializable;

import com.ta2khu75.quiz.model.TargetType;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NotificationId implements Serializable {
	private static final long serialVersionUID = 6760439381782529866L;
	private Long accountId;
	private Long targetId;
	@Enumerated(EnumType.STRING)
	private TargetType targetType;
}
