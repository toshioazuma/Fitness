package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IBoost;

import java.util.Date;

public class Boost implements IBoost {

	@Expose
	public Integer id;

	@Expose
	public Date date;

	@Expose
	public int number;

	@Expose
	@SerializedName("feed_id")
	public Integer feedId;

	@Expose
	@SerializedName("user_id")
	public Integer userId;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public Boost() {

	}

	public Boost(Parcel in) {
		id = in.readInt();
	}

	public static final Creator<Boost> CREATOR = new Creator<Boost>() {
		@Override
		public Boost createFromParcel(Parcel in) {
			return new Boost(in);
		}

		@Override
		public Boost[] newArray(int size) {
			return new Boost[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region IBoost
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Integer getFeedId() {
		return feedId;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
