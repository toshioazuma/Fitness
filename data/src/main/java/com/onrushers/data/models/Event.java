package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.IEventRegisterResult;
import com.onrushers.domain.business.model.ILocation;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.Gender;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Event implements IEvent {

	@Expose
	public Integer id;

	@Expose
	public String title;

	@Expose
	public Double price;

	@Expose
	public String currency;

	@Expose
	public Date date;

	@Expose
	@SerializedName("seats_remaining")
	public Integer placesLeft;

	@Expose
	@SerializedName("nb_max_membres")
	public Integer placesMax;

	@Expose
	@SerializedName("public")
	public Integer publicGender;

	@Expose
	public String description;

	@Expose
	@SerializedName("organizer_word")
	public String organizerWord;

	@Expose
	@SerializedName("organizer_photo_id")
	public String organizerPhotoId;

	@Expose
	@SerializedName("recommended_level")
	public Grade recommendedLevel;

	@Expose
	@SerializedName("organizer_id")
	public String organizerId;

	@Expose
	@SerializedName("users")
	public List<EventRegisterResult> registeredUsers;

	@Expose
	public List<User> heroes;

	@Expose
	@SerializedName("teams_ids")
	public List<Integer> teamsIds;

	@Expose
	@SerializedName("photos_ids")
	public List<Integer> photosIds;

	@Expose
	public Location location;

	@Expose
	@SerializedName("qrcode_participation")
	public String qrCodeParticipation;

	public boolean isMine = false;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public Event() {

	}

	public Event(Parcel in) {
		id = in.readInt();
		title = in.readString();
		price = in.readDouble();
		currency = in.readString();
		date = new Date(in.readLong());
		placesLeft = in.readInt();
		placesMax = in.readInt();
		publicGender = in.readInt();
		description = in.readString();
		organizerWord = in.readString();
		organizerPhotoId = in.readString();
		recommendedLevel = in.readParcelable(Grade.class.getClassLoader());
		organizerId = in.readString();
		registeredUsers = in.readArrayList(User.class.getClassLoader());
		heroes = in.readArrayList(User.class.getClassLoader());
		teamsIds = in.readArrayList(Integer.class.getClassLoader());
		photosIds = in.readArrayList(Integer.class.getClassLoader());
		isMine = in.readInt() == 0 ? false : true;
		location = in.readParcelable(Location.class.getClassLoader());
		qrCodeParticipation = in.readString();
	}

	public static final Creator<Event> CREATOR = new Creator<Event>() {
		@Override
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeDouble(getPrice());
		dest.writeString(currency);
		dest.writeLong(date.getTime());
		dest.writeInt(placesLeft);
		dest.writeInt(placesMax);
		dest.writeInt(publicGender);
		dest.writeString(description);
		dest.writeString(organizerWord);
		dest.writeString(organizerPhotoId);
		dest.writeParcelable(recommendedLevel, flags);
		dest.writeString(organizerId);
		dest.writeList(registeredUsers);
		dest.writeList(heroes);
		dest.writeList(teamsIds);
		dest.writeList(photosIds);
		dest.writeInt(isMine ? 1 : 0);
		dest.writeParcelable(location, flags);
		dest.writeString(qrCodeParticipation);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region IEvent
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Double getPrice() {
		if (price == null) {
			return 0.0;
		}
		return price;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public Integer getPlacesLeft() {
		return placesLeft;
	}

	@Override
	public Integer getPlacesMax() {
		return placesMax;
	}

	@Override
	public Gender getPublic() {
		return Gender.from(publicGender);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getOrganizerWord() {
		return organizerWord;
	}

	@Override
	public String getRecommendedLevel() {

		if (recommendedLevel.getGradeMan().equalsIgnoreCase(recommendedLevel.getGradeWoman())) {
			return recommendedLevel.getGradeMan();
		}

		StringBuilder builder = new StringBuilder();
		builder.append(recommendedLevel.getGradeMan());
		builder.append(" / ");
		builder.append(recommendedLevel.getGradeWoman());
		return builder.toString();
	}

	@Override
	public String getOrganizerId() {
		return organizerId;
	}

	@Override
	public String getOrgnaizerPhotoId() {
		return organizerPhotoId;
	}

	@Override
	public List<? extends IEventRegisterResult> getRegisteredUsers() {
		return registeredUsers;
	}

	@Override
	public List<? extends IUser> getHeroes() {
		return heroes;
	}

	@Override
	public List<Integer> getTeamsIds() {
		return teamsIds;
	}

	@Override
	public List<Integer> getPhotosIds() {
		return photosIds;
	}

	@Override
	public ILocation getLocation() {
		return location;
	}

	@Override
	public String getQRCodeParticipation() {
		return qrCodeParticipation;
	}

	//----------------------------------------------------------------------------------------------
	//endregion


	@Override
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	@Override
	public boolean isMine() {
		return isMine;
	}

	@Override
	public void compareWithUserId(Integer userId) {
		if (registeredUsers == null || registeredUsers.isEmpty()) {
			return;
		}

		Iterator<? extends IEventRegisterResult> iterator = registeredUsers.iterator();
		while (iterator.hasNext()) {
			if (userId.equals(iterator.next().getUser().getId())) {
				isMine = true;
				break;
			}
		}
	}

	@Override
	public boolean isLazy() {
		return false;
	}
}
