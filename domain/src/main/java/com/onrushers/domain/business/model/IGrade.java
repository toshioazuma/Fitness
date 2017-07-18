package com.onrushers.domain.business.model;

import android.os.Parcelable;

public interface IGrade extends Parcelable {

	int getId();

	String getNextLevelPhrase();

	String getGradeMan();

	String getGradeWoman();

	boolean getChronoAnimatedMan();

	boolean getChronoAnimatedWoman();

	int getMinBoostNumber();
}
