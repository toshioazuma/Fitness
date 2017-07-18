package com.onrushers.app.user.list;

import android.os.Parcel;
import android.os.Parcelable;

import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.UserListType;

public class UserListConfiguration implements Parcelable {

	private final UserListType mContextType;
	private final IUser        mWhatUser;
	private final IFeed        mWhatFeed;

	private UserListConfiguration(UserListType contextType, IUser whatUser, IFeed whatFeed) {
		mContextType = contextType;
		mWhatFeed = whatFeed;
		mWhatUser = whatUser;
	}

	public static final UserListConfiguration getFansUserConfiguration(IUser user) {
		return new UserListConfiguration(UserListType.Fans, user, null);
	}

	public static final UserListConfiguration getHerosUserConfiguration(IUser user) {
		return new UserListConfiguration(UserListType.Heros, user, null);
	}

	public static final UserListConfiguration getRushesFeedConfiguration(IFeed feed) {
		return new UserListConfiguration(UserListType.FeedRushes, null, feed);
	}

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public UserListConfiguration(Parcel in) {
		mContextType = UserListType.from(in.readInt());
		mWhatFeed = in.readParcelable(IFeed.class.getClassLoader());
		mWhatUser = in.readParcelable(IUser.class.getClassLoader());
	}

	public static final Creator<UserListConfiguration> CREATOR = new Creator<UserListConfiguration>() {
		@Override
		public UserListConfiguration createFromParcel(Parcel in) {
			return new UserListConfiguration(in);
		}

		@Override
		public UserListConfiguration[] newArray(int size) {
			return new UserListConfiguration[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mContextType.getValue());
		dest.writeParcelable(mWhatFeed, flags);
		dest.writeParcelable(mWhatUser, flags);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	public final UserListType getContextType() {
		return mContextType;
	}

	public final IFeed getWhatFeed() {
		return mWhatFeed;
	}

	public final IUser getWhatUser() {
		return mWhatUser;
	}
}
