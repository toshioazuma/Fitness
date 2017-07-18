package com.onrushers.app.feed.adapters.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.FeedTaggedUsersAdapter;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO: document your custom view class.
 */
public class FeedTagUsersView extends RelativeLayout {

	@Bind(R.id.feed_tag_imageview)
	ImageView mTagImageView;

	@Bind(R.id.feed_tag_users_recycler)
	RecyclerView mTagUsersRecyclerView;

	FeedTaggedUsersAdapter mAdapter;

	public FeedTagUsersView(Context context) {
		super(context);
		init(null, 0);
	}

	public FeedTagUsersView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public FeedTagUsersView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		super.inflate(getContext(), R.layout.view_feed_tag_users, this);
		ButterKnife.bind(this);

		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
			LinearLayoutManager.HORIZONTAL, false);
		mTagUsersRecyclerView.setLayoutManager(layoutManager);

		mAdapter = new FeedTaggedUsersAdapter();
		mTagUsersRecyclerView.setAdapter(mAdapter);
	}

	public void setUsers(List<IUser> usersList) {
		mAdapter.setUsers(usersList);
	}

	public void hideTagImageView() {
		mTagImageView.setVisibility(GONE);
	}
}
