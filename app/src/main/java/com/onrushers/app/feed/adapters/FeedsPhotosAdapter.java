package com.onrushers.app.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.feed.adapters.views.FeedPhotoViewHolder;
import com.onrushers.app.feed.detail.OnFeedDetailListener;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FeedsPhotosAdapter extends RecyclerView.Adapter<FeedPhotoViewHolder> {

	private List<IFeed> mPhotosFeeds;

	private final OnFeedDetailListener mDetailListener;

	public FeedsPhotosAdapter(OnFeedDetailListener detailListener) {
		mDetailListener = detailListener;
	}

	public void appendFeeds(List<IFeed> feeds, int page) {
		if (mPhotosFeeds == null) {
			mPhotosFeeds = new ArrayList<>();
		}
		if (page == 1) {
			mPhotosFeeds.clear();
		}
		mPhotosFeeds.addAll(feeds);
		notifyDataSetChanged();
	}

	public void removeFeedById(final int id) {
		if (mPhotosFeeds == null || mPhotosFeeds.isEmpty()) {
			return;
		}

		Observable.from(mPhotosFeeds)
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
					mPhotosFeeds = newFeedsList;
					notifyDataSetChanged();
				}
			});
	}

	@Override
	public FeedPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

		View itemView = layoutInflater.inflate(R.layout.card_feed_post_photo, parent, false);
		return new FeedPhotoViewHolder(itemView, mDetailListener);
	}

	@Override
	public void onBindViewHolder(FeedPhotoViewHolder holder, int position) {
		IFeed feed = mPhotosFeeds.get(position);
		holder.setFeed(feed);
	}

	@Override
	public int getItemCount() {
		if (mPhotosFeeds != null) {
			return mPhotosFeeds.size();
		}
		return 0;
	}
}
