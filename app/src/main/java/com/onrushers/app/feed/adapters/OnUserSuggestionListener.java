package com.onrushers.app.feed.adapters;

import com.onrushers.domain.business.model.IFeed;

public interface OnUserSuggestionListener {

	void onSkipHeroSuggestion(IFeed feed);

	void onFollowHeroSuggestion(IFeed feed);
}
