package com.onrushers.domain.business.model;

import android.os.Parcelable;

import com.onrushers.domain.business.type.FeedType;

import java.util.Date;
import java.util.List;

public interface IFeed extends Parcelable {

	Integer getId();

	FeedType getType();

	String getContent();

	String getPlace();

	IUser getOwner();

	List<Integer> getPhotos();

	List<Integer> getUserIds();

	List<? extends IUserTag> getTagsUsers();

	Date getCreatedAt();

	Integer getBoostsCount();

	Integer getCommentsCount();

	Boolean isRushed();

	boolean isMine();

	boolean isLazy();

	//region Additional
	//----------------------------------------------------------------------------------------------

	void compareWithUserId(Integer userId);

	void attachRush(IBoost boost);

	IBoost getRush();

	//----------------------------------------------------------------------------------------------
	//endregion
}
