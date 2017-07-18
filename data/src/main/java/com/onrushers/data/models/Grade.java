package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IGrade;

public class Grade implements IGrade {

	@Expose
	public int id;

	@Expose
	@SerializedName(value = "next_level_phrase", alternate = "nextLevelPhrase")
	public String nextLevelPhrase;

	@Expose
	@SerializedName(value = "grade_man", alternate = "gradeMan")
	public String gradeMan;

	@Expose
	@SerializedName(value = "grade_women", alternate = "gradeWomen")
	public String gradeWoman;

	@Expose
	@SerializedName(value = "chrono_animated_man", alternate = "chronoAnimatedMan")
	public boolean chronoAnimatedMan;

	@Expose
	@SerializedName(value = "chrono_animated_women", alternate = "chronoAnimatedWomen")
	public boolean chronoAnimatedWoman;

	@Expose
	@SerializedName(value = "min_boost_number", alternate = "minBoostNumber")
	public int minBoostNumber = 0;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public Grade() {

	}

	public Grade(Parcel in) {
		id = in.readInt();
		gradeMan = in.readString();
		gradeWoman = in.readString();
		chronoAnimatedMan = in.readInt() == 1;
		chronoAnimatedWoman = in.readInt() == 1;
		minBoostNumber = in.readInt();
	}

	public static final Creator<Grade> CREATOR = new Creator<Grade>() {
		@Override
		public Grade createFromParcel(Parcel in) {
			return new Grade(in);
		}

		@Override
		public Grade[] newArray(int size) {
			return new Grade[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(gradeMan);
		dest.writeString(gradeWoman);
		dest.writeInt(chronoAnimatedMan ? 1 : 0);
		dest.writeInt(chronoAnimatedWoman ? 1 : 0);
		dest.writeInt(minBoostNumber);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getNextLevelPhrase() {
		return nextLevelPhrase;
	}

	@Override
	public String getGradeMan() {
		if (gradeMan == null) {
			return "";
		}
		return gradeMan;
	}

	@Override
	public String getGradeWoman() {
		if (gradeWoman == null) {
			return "";
		}
		return gradeWoman;
	}

	@Override
	public boolean getChronoAnimatedMan() {
		return chronoAnimatedMan;
	}

	@Override
	public boolean getChronoAnimatedWoman() {
		return chronoAnimatedWoman;
	}

	@Override
	public int getMinBoostNumber() {
		return minBoostNumber;
	}
}
