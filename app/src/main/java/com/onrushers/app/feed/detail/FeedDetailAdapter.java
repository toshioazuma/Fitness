package com.onrushers.app.feed.detail;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.feed.detail.adapter.FeedDetailViewItemType;
import com.onrushers.app.feed.detail.adapter.FeedDetailViewType;
import com.onrushers.app.feed.detail.adapter.viewholders.ICommentViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.IFeedViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.IUserViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedCommentViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedContentPictureViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedContentRegisterViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedContentTextViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedCountRushCommentViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedHeaderViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedTagUsersViewHolder;
import com.onrushers.app.feed.detail.adapter.viewholders.impl.FeedTimePlaceViewHolder;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Ludovic on 02/09/16.
 */
public class FeedDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private FeedDetailViewItemType[] mViewItemTypes;
	private IFeed                    mFeed;

	private List<IComment> mComments;
	private Set<IUser>     mAssociatedUsers;

	private OnFeedHeaderListener     mFeedHeaderListener;
	private OnCommentDeleteListener  mCommentDeleteListener;
	private OnFeedRushListener       mFeedRushListener;
	private OnFeedRushesViewListener mFeedRushesViewListener;

	private OnFeedDetailReadyListener mOnReadyListener;

	public FeedDetailAdapter(FeedDetailViewType viewType, IFeed feed) {
		mFeed = feed;
		if (viewType != null) {
			mViewItemTypes = viewType.getItemTypes();
		}

		mComments = new ArrayList<>();
		mAssociatedUsers = new HashSet<>();
	}

	public void setOnReadyListener(OnFeedDetailReadyListener listener) {
		mOnReadyListener = listener;
	}

	public void setOnFeedHeaderListener(OnFeedHeaderListener feedHeaderListener) {
		mFeedHeaderListener = feedHeaderListener;
	}

	public void setOnCommentDeleteListener(OnCommentDeleteListener commentDeleteListener) {
		mCommentDeleteListener = commentDeleteListener;
	}

	public void setOnFeedRushListener(OnFeedRushListener feedRushListener) {
		mFeedRushListener = feedRushListener;
	}

	public void setOnFeedRushesViewListener(OnFeedRushesViewListener feedRushesViewListener) {
		mFeedRushesViewListener = feedRushesViewListener;
	}

	public void setViewType(FeedDetailViewType viewType, IFeed feed) {
		mViewItemTypes = viewType.getItemTypes();
		mFeed = feed;
		notifyDataSetChanged();
	}

	public void addComments(List<IComment> comments, int page) {
		if (page == 1) {
			mComments.clear();
		}
		mComments.addAll(comments);
		notifyDataSetChanged();
	}

	public void addUsers(List<IUser> users) {
		mAssociatedUsers.addAll(users);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position > mViewItemTypes.length - 1) {
			/** if position exceed length, put the last index */
			position = mViewItemTypes.length - 1;
		}

		return mViewItemTypes[position].getVal();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		FeedDetailViewItemType viewItemType = FeedDetailViewItemType.fromVal(viewType);

		switch (viewItemType) {
			case UserHeader:
				return FeedHeaderViewHolder.newInstance(parent, mFeedHeaderListener);

			case TagUsers:
				return FeedTagUsersViewHolder.newInstance(parent);

			case TimeAndPlace:
				return FeedTimePlaceViewHolder.newInstance(parent);

			case ContentPicture:
				FeedContentPictureViewHolder pictureViewHolder = FeedContentPictureViewHolder.newInstance(parent);
				mOnReadyListener.onReadyTransitionView(pictureViewHolder.getPictureImageView());
				return pictureViewHolder;

			case ContentText:
				return FeedContentTextViewHolder.newInstance(parent);

			case ContentRegister:
				return FeedContentRegisterViewHolder.newInstance(parent);

			case CountRushAndComment:
				return FeedCountRushCommentViewHolder.newInstance(parent, mFeedRushListener, mFeedRushesViewListener);

			case Comment:
				return FeedCommentViewHolder.newInstance(parent, mCommentDeleteListener);
		}

		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ICommentViewHolder) {
			IComment comment = mComments.get(position - mViewItemTypes.length + 1);
			ICommentViewHolder commentViewHolder = (ICommentViewHolder) holder;
			commentViewHolder.setComment(comment);

			if (commentViewHolder instanceof IUserViewHolder) {
				IUserViewHolder userViewHolder = (IUserViewHolder) commentViewHolder;

				Iterator<IUser> iterator = mAssociatedUsers.iterator();
				while (iterator.hasNext()) {
					IUser user = iterator.next();

					if (user.getId().equals(comment.getUserId())) {
						userViewHolder.setUser(user);
						break;
					}
				}
			}

		} else if (holder instanceof IUserViewHolder) {
			IUserViewHolder userViewHolder = (IUserViewHolder) holder;
			userViewHolder.setUser(mFeed.getOwner());

		} else if (holder instanceof IFeedViewHolder) {
			IFeedViewHolder feedViewHolder = (IFeedViewHolder) holder;
			feedViewHolder.setFeed(mFeed);
		}
	}

	@Override
	public int getItemCount() {
		int itemCount;

		if (mViewItemTypes == null) {
			return 0;
		}

		if (mViewItemTypes[mViewItemTypes.length - 1] == FeedDetailViewItemType.Comment) {
			itemCount = mViewItemTypes.length - 1; /** remove comment item type */
			itemCount += mComments.size();
		} else {
			itemCount = mViewItemTypes.length;
		}
		return itemCount;
	}
}
