package com.onrushers.app.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.views.FeedAvatarChangeViewHolder;
import com.onrushers.app.feed.adapters.views.FeedHeroSuggestionViewHolder;
import com.onrushers.app.feed.adapters.views.FeedPostViewHolder;
import com.onrushers.app.feed.adapters.views.FeedRegisterViewHolder;
import com.onrushers.app.feed.adapters.views.FeedViewHolder;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;
import com.onrushers.domain.business.type.FeedType;
import com.onrushers.domain.business.type.Page;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FeedsAdapter extends RecyclerView.Adapter<FeedViewHolder> {

	private List<IFeed> mFeeds;

	private OnFeedHeaderListener     mFeedHeaderListener;
	private OnFeedRushListener       mFeedRushListener;
	private OnFeedRushesViewListener mFeedRushesViewListener;
	private OnFeedDetailListener     mFeedDetailListener;
	private OnFeedReportListener     mFeedReportListener;
	private OnFeedDeleteListener     mFeedDeleteListener;
	private OnUserDetailListener     mUserDetailListener;
	private OnUserSuggestionListener mUserSuggestionListener;


	public FeedsAdapter(OnFeedHeaderListener feedHeaderListener, OnFeedRushListener feedRushListener,
	                    OnFeedRushesViewListener feedRushesViewListener, OnFeedDetailListener feedDetailListener,
	                    OnFeedReportListener feedReportListener, OnFeedDeleteListener feedDeleteListener,
	                    OnUserDetailListener userDetailListener, OnUserSuggestionListener userSuggestionListener) {

		mFeedHeaderListener = feedHeaderListener;
		mFeedRushListener = feedRushListener;
		mFeedRushesViewListener = feedRushesViewListener;
		mFeedDetailListener = feedDetailListener;
		mFeedReportListener = feedReportListener;
		mFeedDeleteListener = feedDeleteListener;
		mUserDetailListener = userDetailListener;
		mUserSuggestionListener = userSuggestionListener;
	}

	public void appendFeeds(List<IFeed> feeds, int page) {
		if (mFeeds == null) {
			mFeeds = new ArrayList<>();
		}
		if (page <= Page.Min.value()) {
			mFeeds.clear();
		}
		mFeeds.addAll(feeds);

		Collections.sort(mFeeds,
			new Comparator<IFeed>() {
				@Override
				public int compare(IFeed lhs, IFeed rhs) {
					/** if same dates, sort by feed type descending: 6, 5,.. and 1 */
					if (rhs.getCreatedAt().equals(lhs.getCreatedAt())) {
						return lhs.getType().getValue().compareTo(rhs.getType().getValue());
					}
					/** sort by dates descending */
					return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
				}
			});

		notifyDataSetChanged();
	}

	public void removeFeedById(final int id) {
		if (mFeeds == null || mFeeds.isEmpty()) {
			return;
		}

		Observable.from(mFeeds)
			.filter(new Func1<IFeed, Boolean>() {
				@Override
				public Boolean call(IFeed feed) {
					return feed.getId() != id;
				}
			})
			.subscribe(new DefaultSubscriber<IFeed>() {
				List<IFeed> newFeedsList;

				@Override
				public void onStart() {
					newFeedsList = new ArrayList<>();
				}

				@Override
				public void onNext(IFeed feed) {
					newFeedsList.add(feed);
				}

				@Override
				public void onCompleted() {
					mFeeds = newFeedsList;
					notifyDataSetChanged();
				}
			});
	}

	public void skipHeroSuggestion(final IFeed feed) {
		if (mFeeds == null || mFeeds.isEmpty()) {
			return;
		}

		Observable.from(mFeeds).filter(new Func1<IFeed, Boolean>() {
			@Override
			public Boolean call(IFeed f) {
				return !(FeedType.HeroSuggestion == f.getType() && (f.getOwner() != null && feed.getOwner() != null && feed.getOwner().getId().equals(f.getOwner().getId())));
			}
		}).subscribe(new DefaultSubscriber<IFeed>() {
			List<IFeed> newFeedsList;


			@Override
			public void onStart() {
				newFeedsList = new ArrayList<>();
			}

			@Override
			public void onNext(IFeed feed) {
				newFeedsList.add(feed);
			}

			@Override
			public void onCompleted() {
				mFeeds = newFeedsList;
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getItemViewType(int position) {
		return mFeeds.get(position).getType().getValue();
	}

	@Override
	public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

		if (viewType == FeedType.Register.getValue()) {
			/** FeedType.Register */

			View itemView = layoutInflater.inflate(R.layout.card_feed_register, parent, false);
			return new FeedRegisterViewHolder(itemView, null, mFeedRushListener,
				mFeedRushesViewListener, mFeedDetailListener, mFeedReportListener,
				mFeedDeleteListener, mUserDetailListener);

		} else if (viewType == FeedType.AvatarChange.getValue()) {
			/** FeedType.AvatarChange */

			View itemView = layoutInflater.inflate(R.layout.card_feed_register, parent, false);
			return new FeedAvatarChangeViewHolder(itemView, null, mFeedRushListener,
				mFeedRushesViewListener, mFeedDetailListener, mFeedReportListener, null);

		} else if (viewType == FeedType.HeroSuggestion.getValue()) {
			/** FeedType.HeroSuggestion */

			View itemView = layoutInflater.inflate(R.layout.card_feed_hero_suggestion, parent, false);
			return new FeedHeroSuggestionViewHolder(itemView, mUserDetailListener, mUserSuggestionListener);
		}

		/** FeedType.Post */
		View itemView = layoutInflater.inflate(R.layout.card_feed_post, parent, false);
		return new FeedPostViewHolder(itemView, mFeedHeaderListener, mFeedRushListener,
			mFeedRushesViewListener, mFeedDetailListener, mFeedReportListener,
			mFeedDeleteListener);
	}

	@Override
	public void onBindViewHolder(FeedViewHolder holder, int position) {
		IFeed feed = mFeeds.get(position);
		holder.setFeed(feed);
	}

	@Override
	public void onViewRecycled(FeedViewHolder holder) {
		holder.onViewRecycled();
		super.onViewRecycled(holder);
	}

	@Override
	public int getItemCount() {
		if (mFeeds != null) {
			return mFeeds.size();
		}
		return 0;
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);

		mFeedHeaderListener = null;
		mFeedRushListener = null;
		mFeedRushesViewListener = null;
		mFeedDetailListener = null;
		mFeedReportListener = null;
		mFeedDeleteListener = null;
		mUserDetailListener = null;
		mUserSuggestionListener = null;
	}
}
