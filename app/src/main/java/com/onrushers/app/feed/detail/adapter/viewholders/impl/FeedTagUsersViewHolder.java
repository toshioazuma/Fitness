package com.onrushers.app.feed.detail.adapter.viewholders.impl;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.FeedTaggedUsersAdapter;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.model.IUserTag;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Ludovic on 04/09/16.
 */
public class FeedTagUsersViewHolder extends RecyclerView.ViewHolder implements IFeedViewHolder {

	@Bind(R.id.feed_detail_tag_user_container_layout)
	RelativeLayout mContainerLayout;

	@Bind(R.id.feed_detail_tag_users_recycler)
	RecyclerView mTagUsersRecyclerView;

	FeedTaggedUsersAdapter mTaggedUsersAdapter;

	private FeedTagUsersViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);

		LinearLayoutManager layoutManager = new LinearLayoutManager(
			itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
		mTagUsersRecyclerView.setLayoutManager(layoutManager);

		mTaggedUsersAdapter = new FeedTaggedUsersAdapter();
		mTagUsersRecyclerView.setAdapter(mTaggedUsersAdapter);
	}

	public static final FeedTagUsersViewHolder newInstance(ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_feed_detail_tag_users, parent, false);

		return new FeedTagUsersViewHolder(itemView);
	}

	@Override
	public void setFeed(IFeed feed) {
		List tagsUsers = feed.getTagsUsers();

		if (tagsUsers != null && !tagsUsers.isEmpty()) {

			Observable.from((List<IUserTag>) tagsUsers)
				.map(new Func1<IUserTag, IUser>() {
					@Override
					public IUser call(IUserTag userTag) {
						return userTag.getUser();
					}
				})
				.toList()
				.subscribe(new Action1<List<IUser>>() {
					@Override
					public void call(List<IUser> users) {
						mTaggedUsersAdapter.setUsers(users);
						mContainerLayout.setVisibility(View.VISIBLE);
					}
				});
		} else {
			mContainerLayout.setVisibility(View.GONE);
		}
	}
}
