package com.onrushers.app.feed.detail.adapter.viewtypes;

import com.onrushers.app.feed.detail.adapter.FeedDetailViewItemType;
import com.onrushers.app.feed.detail.adapter.FeedDetailViewType;

/**
 * Created by Ludovic on 03/09/16.
 */
public class FeedDetailViewPost implements FeedDetailViewType {

	@Override
	public FeedDetailViewItemType[] getItemTypes() {

		return new FeedDetailViewItemType[] {
			FeedDetailViewItemType.UserHeader,
			FeedDetailViewItemType.ContentPicture,
			FeedDetailViewItemType.TimeAndPlace,
			FeedDetailViewItemType.TagUsers,
			FeedDetailViewItemType.ContentText,
			FeedDetailViewItemType.CountRushAndComment,
			FeedDetailViewItemType.Comment
		};
	}
}
