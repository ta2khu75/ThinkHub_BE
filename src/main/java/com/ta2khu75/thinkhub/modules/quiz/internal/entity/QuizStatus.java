package com.ta2khu75.thinkhub.modules.quiz.internal.entity;

public enum QuizStatus {
    ACTIVE,         // public & usable
    INACTIVE,       // owner hides
    OWNER_DELETED,  // owner soft delete
    ADMIN_DISABLED  // admin enforcement
}
