package com.onrushers.domain.business.lazy;

import android.os.Parcel;

import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserTag;
import com.onrushers.domain.business.type.FeedType;

import java.util.Date;
import java.util.List;

public class LazyFeed implements IFeed {

	private final Integer mType;
	private final Integer mId;

	public LazyFeed(Integer type, Integer id) {
		mType = type;
		mId = id;
	}

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public LazyFeed(Parcel in) {
		mType = in.readInt();
		mId = in.readInt();
	}

	public static final Creator<LazyFeed> CREATOR = new Creator<LazyFeed>() {
		@Override
		public LazyFeed createFromParcel(Parcel in) {
			return new LazyFeed(in);
		}

		@Override
		public LazyFeed[] newArray(int size) {
			return new LazyFeed[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mType);
		dest.writeInt(mId);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@Override
	public Integer getId() {
		return mId;
	}

	@Override
	public FeedType getType() {
		return FeedType.from(mType);
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public String getPlace() {
		return null;
	}

	@Override
	public IUser getOwner() {
		return null;
	}

	@Override
	public List<Integer> getPhotos() {
		return null;
	}

	@Override
	public List<Integer> getUserIds() {
		return null;
	}

	@Override
	public List<? extends IUserTag> getTagsUsers() {
		return null;
	}

	@Override
	public Date getCreatedAt() {
		return null;
	}

	@Override
	public Integer getBoostsCount() {
		return null;
	}

	@Override
	public Integer getCommentsCount() {
		return null;
	}

	@Override
	public Boolean isRushed() {
		return null;
	}

	@Override
	public boolean isMine() {
		return false;
	}

	@Override public boolean isLazy() {
		return true;
	}

	@Override
	public void compareWithUserId(Integer userId) {

	}

	@Override
	public void attachRush(IBoost boost) {

	}

	@Override
	public IBoost getRush() {
		return null;
	}
}
