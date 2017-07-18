package com.onrushers.domain.business.lazy;

import android.os.Parcel;

import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.ILocation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;

import java.util.Date;
import java.util.List;

/**
 * IEvent implementation used in the app side.
 *
 * Use it only to pass event info with id at least.
 */
public class LazyEvent implements IEvent {

	private final Integer mId;
	private final String mTitle;

	public LazyEvent(Integer id, String title) {
		mId = id;
		mTitle = title;
	}

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public LazyEvent(Parcel in) {
		mId = in.readInt();
		mTitle = in.readString();
	}

	public static final Creator<LazyEvent> CREATOR = new Creator<LazyEvent>() {
		@Override
		public LazyEvent createFromParcel(Parcel in) {
			return new LazyEvent(in);
		}

		@Override
		public LazyEvent[] newArray(int size) {
			return new LazyEvent[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mTitle);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Required IEvent implementation
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return mId;
	}

	@Override
	public String getTitle() {
		return mTitle;
	}

	@Override
	public boolean isLazy() {
		return true;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Default IEvent implementation
	//----------------------------------------------------------------------------------------------

	@Override
	public Double getPrice() {
		return null;
	}

	@Override
	public String getCurrency() {
		return null;
	}

	@Override
	public Date getDate() {
		return null;
	}

	@Override
	public Integer getPlacesLeft() {
		return null;
	}

	@Override
	public Integer getPlacesMax() {
		return null;
	}

	@Override
	public Gender getPublic() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getOrganizerWord() {
		return null;
	}

	@Override
	public String getRecommendedLevel() {
		return null;
	}

	@Override
	public String getOrganizerId() {
		return null;
	}

	@Override
	public String getOrgnaizerPhotoId() {
		return null;
	}

	@Override
	public List<? extends IEventRegisterResult> getRegisteredUsers() {
		return null;
	}

	@Override
	public List<? extends IUser> getHeroes() {
		return null;
	}

	@Override
	public List<Integer> getTeamsIds() {
		return null;
	}

	@Override
	public List<Integer> getPhotosIds() {
		return null;
	}

	@Override
	public ILocation getLocation() {
		return null;
	}

	@Override
	public String getQRCodeParticipation() {
		return null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@Override
	public void setMine(boolean isMine) {

	}

	@Override
	public boolean isMine() {
		return false;
	}

	@Override
	public void compareWithUserId(Integer userId) {

	}
}
