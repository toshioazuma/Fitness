package com.onrushers.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onrushers.domain.business.model.IFeedReport;

public class FeedReport extends Report implements IFeedReport {

	@Expose
	@SerializedName("feed_id")
	public Integer feedId;


	@Override
	public Integer getFeedId() {
		return feedId;
	}
}
