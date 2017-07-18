package com.onrushers.data.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserTag;
import com.onrushers.domain.business.type.FeedType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed implements IFeed {

	@Expose
	public Integer id;

	@Expose
	public Integer type;

	@Expose
	@SerializedName("text")
	public String content;

	@Expose
	public String place;

	@Expose
	@SerializedName(value = "owner", alternate = {"owner_id"})
	public Integer userId;

	@Expose
	@SerializedName("owner_infos")
	public User user;

	@Expose
	@SerializedName(value = "photos", alternate = {"photos_ids"})
	public List<Integer> photos = new ArrayList<>();

	@Expose
	@SerializedName("users_ids")
	public List<Integer> userIds = new ArrayList<>();

	@Expose
	@SerializedName("tags")
	public List<Integer> userTags = new ArrayList<>();

	@Expose
	@SerializedName("tags_users")
	public List<UserTag> tagsUsers = new ArrayList<>();

	@Expose
	public Date date;

	@Expose
	@SerializedName("boosts_count")
	public Integer boostsCount;

	@Expose
	@SerializedName("comments_count")
	public Integer commentsCount;

	@Expose
	public Integer rushed = 0;

	private boolean isMine = false;


	//region Parcelable
	//----------------------------------------------------------------------------------------------

	public Feed() {

	}

	public Feed(Parcel in) {
		id = in.readInt();
		type = in.readInt();
		content = in.readString();
		place = in.readString();
		user = in.readParcelable(User.class.getClassLoader());
		photos = in.readArrayList(Integer.class.getClassLoader());
		userIds = in.readArrayList(Integer.class.getClassLoader());
		userTags = in.readArrayList(Integer.class.getClassLoader());
		tagsUsers = in.readArrayList(UserTag.class.getClassLoader());
		date = new Date(in.readLong());
		boostsCount = in.readInt();
		commentsCount = in.readInt();
		rushed = in.readInt();
		isMine = in.readInt() == 1;
	}

	public static final Creator<Feed> CREATOR = new Creator<Feed>() {
		@Override
		public Feed createFromParcel(Parcel in) {
			return new Feed(in);
		}

		@Override
		public Feed[] newArray(int size) {
			return new Feed[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(type);
		dest.writeString(content);
		dest.writeString(place);
		dest.writeParcelable(user, flags);
		dest.writeList(photos);
		dest.writeList(userIds);
		dest.writeList(userTags);
		dest.writeList(tagsUsers);
		dest.writeLong(date.getTime());
		dest.writeInt(boostsCount);
		dest.writeInt(commentsCount);
		dest.writeInt(rushed != null ? rushed : 0);
		dest.writeInt(isMine ? 1 : 0);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region IFeed
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public FeedType getType() {
		return FeedType.from(type);
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getPlace() {
		return place;
	}

	@Override
	public IUser getOwner() {
		return user;
	}

	@Override
	public List<Integer> getPhotos() {
		return photos;
	}

	@Override
	public List<Integer> getUserIds() {
		return userIds;
	}

	@Override
	public List<? extends IUserTag> getTagsUsers() {
		return tagsUsers;
	}

	@Override
	public Date getCreatedAt() {
		return date;
	}

	@Override
	public Integer getBoostsCount() {
		return boostsCount;
	}

	@Override
	public Integer getCommentsCount() {
		return commentsCount;
	}

	@Override
	public Boolean isRushed() {
		return (rushed != null && rushed > 0);
	}

	@Override
	public boolean isMine() {
		return isMine;
	}

	@Override public boolean isLazy() {
		return false;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Additional
	//----------------------------------------------------------------------------------------------

	@Override
	public void compareWithUserId(Integer userId) {
		if (user != null) {
			isMine = user.getId().equals(userId);
		}
	}

	@Override
	public void attachRush(IBoost boost) {
		if (boost != null) {
			rushed = boost.getId();
			boostsCount++;
		} else {
			rushed = null;
			boostsCount--;
		}
	}

	public IBoost getRush() {
		if (rushed != null && rushed > 0) {
			Boost boost = new Boost();
			boost.id = rushed;
			return boost;
		}
		return null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
