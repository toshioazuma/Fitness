package com.onrushers.common.widgets.appbarlayout;

import android.support.design.widget.AppBarLayout;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

	public enum State {
		EXPANDED,
		COLLAPSED,
		MIN_COLLAPSED,
		IDLE
	}

	private State mCurrentState       = State.IDLE;
	private int   mMinCollapsedHeight = 0;

	public AppBarStateChangeListener() {

	}

	public AppBarStateChangeListener(int minCollapsedHeight) {
		mMinCollapsedHeight = minCollapsedHeight;
	}

	@Override
	public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

		if (verticalOffset == 0) {
			if (mCurrentState != State.EXPANDED) {
				onStateChanged(appBarLayout, State.EXPANDED);
			}
			mCurrentState = State.EXPANDED;
		} else if (mMinCollapsedHeight > 0 && Math.abs(verticalOffset) + mMinCollapsedHeight >= appBarLayout.getTotalScrollRange()) {

			if (mCurrentState != State.MIN_COLLAPSED) {
				onStateChanged(appBarLayout, State.MIN_COLLAPSED);
			}
			mCurrentState = State.MIN_COLLAPSED;

		} else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
			if (mCurrentState != State.COLLAPSED) {
				onStateChanged(appBarLayout, State.COLLAPSED);
			}
			mCurrentState = State.COLLAPSED;
		} else {
			if (mCurrentState != State.IDLE) {
				onStateChanged(appBarLayout, State.IDLE);
			}
			mCurrentState = State.IDLE;
		}
	}

	public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
