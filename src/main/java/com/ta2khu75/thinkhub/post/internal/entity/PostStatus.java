package com.ta2khu75.thinkhub.post.internal.entity;

public enum PostStatus {
	ACTIVE, // public & usable
	INACTIVE, // owner hides
	OWNER_DELETED, // owner soft delete
	ADMIN_DISABLED // admin enforcement
}
