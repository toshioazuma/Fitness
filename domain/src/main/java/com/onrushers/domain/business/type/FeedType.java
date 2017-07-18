package com.onrushers.domain.business.type;

public enum FeedType {

	Any(-1),
	Post(0),
	Event(1),
	LevelChange(2),
	TrophyWin(3),
	SessionEnd(4),
	Ad(5),
	Register(6),
	AvatarChange(7),
	HeroSuggestion(8);

	private Integer value;

	FeedType(Integer val) {
		this.value = val;
	}

	public Integer getValue() {
		return this.value;
	}

	public static FeedType from(Integer val) {
		if (Post.getValue().equals(val)) {
			return Post;
		} else if (Event.getValue().equals(val)) {
			return Event;
		} else if (LevelChange.getValue().equals(val)) {
			return LevelChange;
		} else if (TrophyWin.getValue().equals(val)) {
			return TrophyWin;
		} else if (SessionEnd.getValue().equals(val)) {
			return SessionEnd;
		} else if (Ad.getValue().equals(val)) {
			return Ad;
		} else if (Register.getValue().equals(val)) {
			return Register;
		} else if (AvatarChange.getValue().equals(val)) {
			return AvatarChange;
		} else if (HeroSuggestion.getValue().equals(val)) {
			return HeroSuggestion;
		}
		return Any;
	}
}
