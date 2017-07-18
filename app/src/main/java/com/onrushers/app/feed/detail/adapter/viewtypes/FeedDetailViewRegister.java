package com.onrushers.app.feed.detail.adapter.viewtypes;

import com.onrushers.app.feed.detail.adapter.FeedDetailViewItemType;
import com.onrushers.app.feed.detail.adapter.FeedDetailViewType;

/**
 * Created by Ludovic on 03/09/16.
 */
public class FeedDetailViewRegister implements FeedDetailViewType {

	@Override
	public FeedDetailViewItemType[] getItemTypes() {

		return new FeedDetailViewItemType[] {
			FeedDetailViewItemType.ContentRegister,
			FeedDetailViewItemType.ContentText,
			FeedDetailViewItemType.TimeAndPlace,
			FeedDetailViewItemType.CountRushAndComment,
			FeedDetailViewItemType.Comment
		};
	}
}
