package com.onrushers.app.explore.tabs.ranking.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Font;

public class RankPositionTextView extends TextView {

	private Bitmap mBackgroundBitmap;
	private int    mRankPosition;

	private Paint mRankPositionPaint;

	public RankPositionTextView(Context context) {
		super(context);
		setup();
	}

	public RankPositionTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public RankPositionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setup();
	}

	private void setup() {
		final Context ctx = getContext();

		mRankPositionPaint = new Paint();
		mRankPositionPaint.setColor(ContextCompat.getColor(ctx, R.color.darkGray));
		mRankPositionPaint.setTypeface(Typeface.createFromAsset(ctx.getAssets(), Font.MONTSERRAT_BOLD));
		mRankPositionPaint.setTextSize(getTextSize());
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		float centerX = width / 2f;
		float centerY = height / 2f;
		float deltaTextY = 0f;

		if (mBackgroundBitmap != null) {
			float bitmapX = centerX - mBackgroundBitmap.getWidth() / 2;
			float bitmapY = centerY - mBackgroundBitmap.getHeight() / 2;
			canvas.drawBitmap(mBackgroundBitmap, bitmapX, bitmapY, null);
			deltaTextY = mBackgroundBitmap.getHeight() / 3.5f; // arbitrary value
		}

		String rankText = String.valueOf(mRankPosition);

		Rect textBounds = new Rect();
		mRankPositionPaint.getTextBounds(rankText, 0, rankText.length(), textBounds);

		float textWidth = mRankPositionPaint.measureText(rankText);

		float originalTextSize = getTextSize();
		while (textWidth > width) {
			mRankPositionPaint.setTextSize(--originalTextSize);
			mRankPositionPaint.getTextBounds(rankText, 0, rankText.length(), textBounds);
			textWidth = mRankPositionPaint.measureText(rankText);
		}

		float textHeight = textBounds.height();

		canvas.drawText(rankText, centerX - (textWidth / 2f), centerY + (textHeight / 2f) - deltaTextY, mRankPositionPaint);
	}

	public void setRankPosition(int rankPosition) {
		mRankPosition = rankPosition;
		int resId = 0;

		switch (rankPosition) {
			case 1:
				resId = R.drawable.ic_rank_one;
				break;
			case 2:
				resId = R.drawable.ic_rank_two;
				break;
			case 3:
				resId = R.drawable.ic_rank_three;
				break;
			case 4:
				resId = R.drawable.ic_rank_four;
				break;
			case 5:
				resId = R.drawable.ic_rank_five;
				break;
			case 6:
				resId = R.drawable.ic_rank_six;
				break;
			default:
				break;
		}

		if (resId > 0) {
			mBackgroundBitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
		} else {
			mBackgroundBitmap = null;
		}

		invalidate();
	}
}
