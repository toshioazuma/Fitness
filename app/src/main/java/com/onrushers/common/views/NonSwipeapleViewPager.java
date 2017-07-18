package com.onrushers.common.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * TODO: document your custom view class.
 */
public class NonSwipeapleViewPager extends ViewPager {

	public NonSwipeapleViewPager(Context context) {
		super(context);
	}

	public NonSwipeapleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
}
