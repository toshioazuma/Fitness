package com.onrushers.app.feed.detail;

import android.widget.ImageView;

import com.onrushers.domain.business.model.IFeed;

public interface OnFeedDetailListener {

	void onFeedDetail(IFeed feed, ImageView source);
}
