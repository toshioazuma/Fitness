package com.onrushers.domain.business.lazy;

import android.os.Parcel;

import com.onrushers.domain.business.model.IGrade;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;

import java.util.Date;
import java.util.List;

public class LazyUser implements IUser {

	private final Integer mId;

	public LazyUser(Integer id) {
		mId = id;
	}

	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public LazyUser(Parcel in) {
		mId = in.readInt();
	}

	public static final Creator<LazyUser> CREATOR = new Creator<LazyUser>() {
		@Override
		public LazyUser createFromParcel(Parcel in) {
			return new LazyUser(in);
		}

		@Override
		public LazyUser[] newArray(int size) {
			return new LazyUser[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Required IUser implementation
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return mId;
	}

	@Override
	public boolean isLazy() {
		return true;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Default IUser implementation
	//----------------------------------------------------------------------------------------------

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public String getCountry() {
		return null;
	}

	@Override
	public String getFirstName() {
		return null;
	}

	@Override
	public String getLastName() {
		return null;
	}

	@Override
	public Date getBirthdate() {
		return null;
	}

	@Override
	public Date getRegistrationDate() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Integer getPoints() {
		return null;
	}

	@Override
	public Gender getGender() {
		return null;
	}

	@Override
	public Integer getBoostCount() {
		return null;
	}

	@Override
	public String getFacebookId() {
		return null;
	}

	@Override
	public IGrade getGrade() {
		return null;
	}

	@Override
	public Object getParameters() {
		return null;
	}

	@Override
	public List getPhotos() {
		return null;
	}

	@Override
	public String getProfilePicture() {
		return null;
	}

	@Override
	public String getCoverPicture() {
		return null;
	}

	@Override
	public List getBadgesIds() {
		return null;
	}

	@Override
	public List getGroupsIds() {
		return null;
	}

	@Override
	public List getTeamsIds() {
		return null;
	}

	@Override
	public Integer getFansCount() {
		return null;
	}

	@Override
	public Integer getHerosCount() {
		return null;
	}

	@Override
	public Integer getRank() {
		return null;
	}

	@Override
	public IRelation getHeroRelation() {
		return null;
	}

	@Override
	public IRelation getFanRelation() {
		return null;
	}

	@Override
	public String getGradeString() {
		return null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
