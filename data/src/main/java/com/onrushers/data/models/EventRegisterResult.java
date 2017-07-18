package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.IUser;

public class EventRegisterResult implements IEventRegisterResult {

	@Expose
	public Integer id;

	@Expose
	public String email;

	@Expose
	public Event event;

	@Expose
	public User user;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public EventRegisterResult() {

	}

	public EventRegisterResult(Parcel in) {
		id = in.readInt();
		user = in.readParcelable(User.class.getClassLoader());
	}

	public static final Creator<EventRegisterResult> CREATOR = new Creator<EventRegisterResult>() {
		@Override
		public EventRegisterResult createFromParcel(Parcel in) {
			return new EventRegisterResult(in);
		}

		@Override
		public EventRegisterResult[] newArray(int size) {
			return new EventRegisterResult[size];
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

	//region IEventRegisterResult
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public IEvent getEvent() {
		return event;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
