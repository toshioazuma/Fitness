package com.onrushers.domain.business.type;

public enum Page {

	Min(1);

	private Integer value;

	Page(Integer val) {
		this.value = val;
	}

	public Integer value() {
		return this.value;
	}
}
