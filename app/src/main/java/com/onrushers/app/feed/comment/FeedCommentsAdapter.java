package com.onrushers.app.feed.comment;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedCommentViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedHeaderViewHolder;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;

import java.util.ArrayList;
import java.util.List;

public class FeedCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_FEED_HEADER = 0;
	private static final int TYPE_COMMENT_ROW = 1;

	private IFeed          mFeed;
	private List<IComment> mComments;


	public FeedCommentsAdapter() {

	}

	public void setFeed(IFeed feed) {
		mFeed = feed;
	}

	public void appendComments(List<IComment> comments, int page) {
		if (mComments == null) {
			mComments = new ArrayList<>();
		}
		if (page == 1) {
			mComments.clear();
		}
		mComments.addAll(comments);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_FEED_HEADER;
		}
		return TYPE_COMMENT_ROW;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		/**
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

		if (viewType == TYPE_FEED_HEADER) {
			View itemView = layoutInflater.inflate(R.layout.card_feed_comment_header, parent, false);
			return new FeedHeaderViewHolder(itemView);
		} else {
			View itemView = layoutInflater.inflate(R.layout.card_feed_comment_row, parent, false);
			return new FeedCommentViewHolder(itemView, mCommentActionListener);
		}
		*/

		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (holder.getItemViewType()) {
			case TYPE_FEED_HEADER:
				FeedHeaderViewHolder headerHolder = (FeedHeaderViewHolder) holder;
				headerHolder.setFeed(mFeed);
				break;

			case TYPE_COMMENT_ROW:
				FeedCommentViewHolder commentHolder = (FeedCommentViewHolder) holder;
				IComment comment = mComments.get(position - 1);
				commentHolder.setComment(comment);
				break;
		}
	}

	@Override
	public int getItemCount() {
		int count = 1;
		if (mComments != null) {
			count += mComments.size();
		}
		return count;
	}
}
