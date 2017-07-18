package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserPagination;

import java.util.List;

public class UserPagination implements IUserPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<User> users;


	//region IUserPagination
	//----------------------------------------------------------------------------------------------

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public int getPages() {
		return pages;
	}

	@Override
	public List<? extends IUser> getItems() {
		return users;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
