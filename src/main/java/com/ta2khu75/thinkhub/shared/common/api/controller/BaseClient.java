package com.ta2khu75.thinkhub.shared.common.api.controller;

public abstract class BaseClient<T> {
	protected T api;

	protected BaseClient(T api) {
		super();
		this.api = api;
	}
}
