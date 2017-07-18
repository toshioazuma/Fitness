package com.onrushers.domain.business.model;

import com.onrushers.domain.business.type.NotificationType;

import java.util.Date;

public interface INotification {

	int getId();

	String getText();

	boolean isRead();

	Date getDate();

	IUser getFromUser();

	NotificationType getNotificationType();

	Integer getTypeId();

	Integer getPostPictureId();
}
