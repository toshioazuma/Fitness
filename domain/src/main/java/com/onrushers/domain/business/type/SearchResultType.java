package com.onrushers.domain.business.type;

public enum SearchResultType {

	User("user"),
	Event("event"),
	Any(null);

	private String value;

	SearchResultType(String val) {
		this.value = val;
	}

	public String getValue() {
		return this.value;
	}

	public static SearchResultType from(String val) {
		if (Event.getValue().equals(val)) {
			return Event;
		}
		return User;
	}
}
