package com.ta2khu75.thinkhub.shared.common.api.controller;

public abstract class BaseController<Api> {
	protected Api api;

	protected BaseController(Api api) {
		super();
		this.api = api;
	}
}
