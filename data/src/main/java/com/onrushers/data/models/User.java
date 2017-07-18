package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IGrade;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class User implements IUser {

	@Expose
	public Integer id;

	@Expose
	public String username;

	@Expose
	@SerializedName("name")
	public String firstName;

	@Expose
	@SerializedName("surname")
	public String lastName;

	@Expose
	public String email;

	@Expose
	public String password;

	@Expose
	@SerializedName("profile_picture_id")
	public String photoId;

	@Expose
	@SerializedName("cover_picture_id")
	public String coverPictureId;

	@Expose
	@SerializedName("birthdate")
	public String birthDate;

	@Expose
	@SerializedName("genre")
	public Integer gender;

	@Expose
	public String country;

	@Expose
	@SerializedName(value = "boosts_count", alternate = "boostsCount")
	public Integer boostsCount;

	@Expose
	public Integer points;

	@Expose
	@SerializedName("facebook_id")
	public String facebookId;

	@Expose
	public Grade grade;

	@Expose
	public String description;

	@Expose
	@SerializedName("nb_fans")
	public Integer fansCount = 0;

	@Expose
	@SerializedName("nb_heros")
	public Integer herosCount = 0;

	@Expose
	public Integer rank;

	@Expose
	@SerializedName("hero_relation")
	public Relation heroRelation;

	@Expose
	@SerializedName("fan_relation")
	public Relation fanRelation;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public User() {

	}

	public User(Parcel in) {
		id = in.readInt();
		username = in.readString();
		firstName = in.readString();
		lastName = in.readString();
		photoId = in.readString();
		coverPictureId = in.readString();
		description = in.readString();
		grade = in.readParcelable(Grade.class.getClassLoader());
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(photoId);
		dest.writeString(coverPictureId);
		dest.writeString(description);
		dest.writeParcelable(grade, flags);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region IUser
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public Date getBirthdate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			return dateFormat.parse(birthDate);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Date getRegistrationDate() {
		return null;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Integer getPoints() {
		return points;
	}

	@Override
	public Gender getGender() {
		return Gender.from(gender);
	}

	@Override
	public Integer getBoostCount() {
		return boostsCount;
	}

	@Override
	public String getFacebookId() {
		return facebookId;
	}

	@Override
	public IGrade getGrade() {
		return grade;
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
		if (photoId != null && photoId.length() > 0) {
			return photoId;
		}
		return null;
	}

	@Override
	public String getCoverPicture() {
		if (coverPictureId != null && coverPictureId.length() > 0) {
			return coverPictureId;
		}
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
		return fansCount;
	}

	@Override
	public Integer getHerosCount() {
		return herosCount;
	}

	@Override
	public Integer getRank() {
		return rank;
	}

	@Override
	public IRelation getHeroRelation() {
		return heroRelation;
	}

	@Override
	public IRelation getFanRelation() {
		return fanRelation;
	}

	@Override
	public String getGradeString() {
		if (grade != null) {
			if (gender == Gender.Female.getValue()) {
				return grade.getGradeWoman();
			} else {
				return grade.getGradeMan();
			}
		}
		return null;
	}

	@Override
	public boolean isLazy() {
		return false;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
