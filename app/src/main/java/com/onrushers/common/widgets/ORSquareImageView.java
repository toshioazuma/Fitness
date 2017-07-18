package com.onrushers.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ORSquareImageView extends ImageView {

	public ORSquareImageView(Context context) {
		super(context);
	}

	public ORSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ORSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ORSquareImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		setMeasuredDimension(width, width);
	}
}
