package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.INotification;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.NotificationType;

import java.util.Date;

public class Notification implements INotification {

	@Expose
	public int id;

	@Expose
	public String text;

	@Expose
	public boolean read = false;

	@Expose
	public Date date;

	@Expose
	@SerializedName("from_user")
	public User fromUser;

	@Expose
	public String type;

	@Expose
	@SerializedName("type_id")
	public Integer typeId;

	@Expose
	@SerializedName("picture_id")
	public Integer pictureId;


	public Notification() {

	}

	//region INotification
	//----------------------------------------------------------------------------------------------

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public boolean isRead() {
		return read;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public IUser getFromUser() {
		return fromUser;
	}

	@Override
	public NotificationType getNotificationType() {
		return NotificationType.from(type);
	}

	@Override
	public Integer getTypeId() {
		return typeId;
	}

	@Override public Integer getPostPictureId() {
		return pictureId;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
