package com.ta2khu75.thinkhub.quiz.internal.entity;

public enum QuizStatus {
	DRAFT,          // owner editing
    ACTIVE,         // public & usable
    INACTIVE,       // owner hides
    OWNER_DELETED,  // owner soft delete
    ADMIN_DISABLED  // admin enforcement
}
