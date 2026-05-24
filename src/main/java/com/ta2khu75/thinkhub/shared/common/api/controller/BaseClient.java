package com.ta2khu75.thinkhub.shared.common.api.controller;

public abstract class BaseClient<Api> {
	protected Api api;

	protected BaseClient(Api api) {
		super();
		this.api = api;
	}
}
