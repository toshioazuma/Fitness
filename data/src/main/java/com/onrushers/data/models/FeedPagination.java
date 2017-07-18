package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IFeedPagination;

import java.util.List;

public class FeedPagination implements IFeedPagination {

	@Expose
	public int count;

	@Expose
	public int pages;

	@Expose
	@SerializedName("list")
	public List<Feed> feeds;


	//region IFeedPagination
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
	public List<? extends IFeed> getItems() {
		return feeds;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
