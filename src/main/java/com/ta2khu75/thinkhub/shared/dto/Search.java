package com.ta2khu75.thinkhub.shared.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Search {
	private String keyword;
	private int page = 0;
	private int size = 10;
	private String sortBy = "createdAt"; // mặc định
	private String direction = "desc"; // asc hoặc desc

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		if (!direction.equals("asc") && !direction.equals("desc")) return;
		this.direction = direction;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page > 0) {
			this.page = page - 1;
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size > 0 && size < 51) {
			this.size = size;
		}
	}

	public Pageable toPageable() {
		Sort.Direction dir = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.DESC);
		Sort sort = Sort.by(dir, sortBy);
		return PageRequest.of(page, size, sort);
	}
}
