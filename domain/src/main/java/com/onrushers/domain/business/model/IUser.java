package com.onrushers.domain.business.model;

import android.os.Parcelable;

import com.onrushers.domain.business.type.Gender;

import java.util.Date;
import java.util.List;

public interface IUser extends Parcelable {

	Integer getId();

	String getUsername();

	String getEmail();

	String getCountry();

	String getFirstName();

	String getLastName();

	Date getBirthdate();

	Date getRegistrationDate();

	String getDescription();

	Integer getPoints();

	Gender getGender();

	Integer getBoostCount();

	String getFacebookId();

	IGrade getGrade();

	Object getParameters();

	List getPhotos();

	String getProfilePicture();

	String getCoverPicture();

	List getBadgesIds();

	List getGroupsIds();

	List getTeamsIds();

	Integer getFansCount();

	Integer getHerosCount();

	Integer getRank();

	IRelation getHeroRelation();

	IRelation getFanRelation();

	// extras

	String getGradeString();

	boolean isLazy();
}
