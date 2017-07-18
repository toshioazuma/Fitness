package com.onrushers.app.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.onrushers.app.feed.adapters.views.FeedTaggedUserViewHolder;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.IUser;

import java.lang.ref.WeakReference;
import java.util.List;

public class FeedTaggedUsersAdapter extends RecyclerView.Adapter<FeedTaggedUserViewHolder> {

	private List<IUser> mUsers;

	public void setUsers(List<IUser> users) {
		mUsers = users;
		notifyDataSetChanged();
	}

	@Override
	public FeedTaggedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return FeedTaggedUserViewHolder.newInstance(parent);
	}

	@Override
	public void onBindViewHolder(FeedTaggedUserViewHolder holder, int position) {
		IUser user = mUsers.get(position);
		holder.setUser(user);
	}

	@Override
	public int getItemCount() {
		if (mUsers != null) {
			return mUsers.size();
		}
		return 0;
	}
}
