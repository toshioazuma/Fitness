package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IUser;

import java.util.Date;

public class Comment implements IComment {

	@Expose
	public Integer id;

	@Expose
	public String text;

	@Expose
	public Date date;

	@Expose
	@SerializedName("feed_id")
	public Integer feedId;

	@Expose
	@SerializedName("user_id")
	public Integer userId;

	public boolean isMine = false;


	//region IComment
	//----------------------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public Integer getFeedId() {
		return feedId;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}

	@Override
	public boolean isMine() {
		return isMine;
	}

	@Override
	public void compareWithUserId(Integer aUserId) {
		isMine = userId.equals(aUserId);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
