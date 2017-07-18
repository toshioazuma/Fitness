package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserTag;

public class UserTag implements IUserTag {

	@Expose
	public Integer id;

	@Expose
	@SerializedName("user_tag")
	public User user;

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public UserTag() {

	}

	public UserTag(Parcel in) {
		id = in.readInt();
		user = in.readParcelable(User.class.getClassLoader());
	}

	public static final Creator<UserTag> CREATOR = new Creator<UserTag>() {
		@Override
		public UserTag createFromParcel(Parcel in) {
			return new UserTag(in);
		}

		@Override
		public UserTag[] newArray(int size) {
			return new UserTag[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeParcelable(user, flags);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region IUserTag
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
