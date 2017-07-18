package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.SearchResultType;

public class ResultResultItem implements ISearchResult {

	@Expose
	public String type;

	@Expose
	public Integer id;

	@Expose
	public String name;

	@Expose
	public String photo;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public ResultResultItem() {

	}

	public ResultResultItem(Parcel in) {
		id = in.readInt();
		type = in.readString();
		name = in.readString();
		photo = in.readString();
	}

	public static final Creator<ResultResultItem> CREATOR = new Creator<ResultResultItem>() {
		@Override
		public ResultResultItem createFromParcel(Parcel in) {
			return new ResultResultItem(in);
		}

		@Override
		public ResultResultItem[] newArray(int size) {
			return new ResultResultItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(photo);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region ISearchResult
	//----------------------------------------------------------------------------------------------

	@Override
	public SearchResultType getType() {
		return SearchResultType.from(type);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getPhoto() {
		return null;
	}

	@Override
	public IUser toUser() {
		User convertedUser = new User();
		convertedUser.id = id;
		convertedUser.username = name;
		convertedUser.photoId = photo;
		convertedUser.firstName = "";
		convertedUser.lastName = "";
		convertedUser.description = "";
		return convertedUser;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	@Override
	public boolean equals(Object o) {
		if (o instanceof ISearchResult) {
			ISearchResult other = (ISearchResult) o;

			if (type.equals(other.getType().getValue()) && id.intValue() == other.getId().intValue()) {
				return true;
			}
		} else if (o instanceof IUser) {
			IUser user = (IUser) o;

			if (type.equals(SearchResultType.User.getValue()) && id.intValue() == user.getId().intValue()) {
				return true;
			}
		}
		return false;
	}
}
