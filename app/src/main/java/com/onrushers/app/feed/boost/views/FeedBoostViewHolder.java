package com.onrushers.app.feed.boost.views;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.domain.business.model.IBoost;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedBoostViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.card_feed_boost_user_textview)
	TextView mUserTextView;

	@Bind(R.id.card_feed_boost_value_textview)
	TextView mValueTextView;

	@Bind(R.id.card_feed_boost_date_textview)
	TextView mDateTextView;


	public FeedBoostViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public void setBoost(IBoost boost) {
		mUserTextView.setText("User ID: " + boost.getUserId());
		mValueTextView.setText("Value: " + boost.getNumber());

		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(boost.getDate().getTime());
		mDateTextView.setText(timeAgo);
	}
}
