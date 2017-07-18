package com.onrushers.domain.business.model;

import android.os.Parcelable;

import com.onrushers.domain.business.type.Gender;

import java.util.Date;
import java.util.List;

public interface IEvent extends Parcelable {

	Integer getId();

	String getTitle();

	Double getPrice();

	String getCurrency();

	Date getDate();

	Integer getPlacesLeft();

	Integer getPlacesMax();

	Gender getPublic();

	String getDescription();

	String getOrganizerWord();

	String getRecommendedLevel();

	String getOrganizerId();

	String getOrgnaizerPhotoId();

	List<? extends IEventRegisterResult> getRegisteredUsers();

	List<? extends IUser> getHeroes();

	List<Integer> getTeamsIds();

	List<Integer> getPhotosIds();

	ILocation getLocation();

	String getQRCodeParticipation();

	void setMine(boolean isMine);

	boolean isMine();

	void compareWithUserId(Integer userId);

	boolean isLazy();
}
