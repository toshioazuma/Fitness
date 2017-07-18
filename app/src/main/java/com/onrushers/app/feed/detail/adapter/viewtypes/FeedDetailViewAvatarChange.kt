package com.onrushers.app.feed.detail.adapter.viewtypes

import com.onrushers.app.feed.detail.adapter.FeedDetailViewItemType
import com.onrushers.app.feed.detail.adapter.FeedDetailViewType

class FeedDetailViewAvatarChange : FeedDetailViewType {

    override fun getItemTypes(): Array<FeedDetailViewItemType> {

        return arrayOf(
                FeedDetailViewItemType.ContentRegister,
                FeedDetailViewItemType.ContentText,
                FeedDetailViewItemType.TimeAndPlace,
                FeedDetailViewItemType.CountRushAndComment,
                FeedDetailViewItemType.Comment
        )
    }
}
