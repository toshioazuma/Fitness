package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IRankPagination;
import com.onrushers.domain.business.model.IUser;

public class RankPagination extends Pagination<User> implements IRankPagination {

	@Expose
	@SerializedName("myRank")
	User myRankUser;

	@Override
	public IUser getMyRankUser() {
		return myRankUser;
	}
}
