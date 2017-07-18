package com.onrushers.domain.business.type;

public enum UserListType {

	Fans(0),
	Heros(1),
	FeedRushes(2);

	private int value;

	UserListType(int val) {
		this.value = val;
	}

	public int getValue() {
		return this.value;
	}

	public static UserListType from(int val) {
		if (Fans.getValue() == val) {
			return Fans;
		} else if (Heros.getValue() == val) {
			return Heros;
		} else if (FeedRushes.getValue() == val) {
			return FeedRushes;
		}
		return Fans;
	}
}
