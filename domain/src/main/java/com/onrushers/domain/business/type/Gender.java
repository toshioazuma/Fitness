package com.onrushers.domain.business.type;

public enum Gender {

	Male(0),
	Female(1),
	Public(2);

	private Integer value;

	Gender(Integer val) {
		this.value = val;
	}

	public Integer getValue() {
		return this.value;
	}

	public static Gender from(Integer val) {
		if (Female.getValue() == val) {
			return Female;
		} else if (Public.getValue() == val) {
			return Public;
		}
		return Male;
	}

	@Override
	public String toString() {
		if (this == Male) {
			return "male";
		} else if (this == Female) {
			return "female";
		}
		return super.toString();
	}
}
