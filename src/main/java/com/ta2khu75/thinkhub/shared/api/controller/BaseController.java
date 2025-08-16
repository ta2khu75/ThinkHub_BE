package com.ta2khu75.thinkhub.shared.api.controller;

public abstract class BaseController<Service> {
	protected Service service;
	protected BaseController(Service service) {
		super();
		this.service = service;
	}
}
