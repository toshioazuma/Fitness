package com.onrushers.app.feed.boost;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.feed.boost.views.FeedBoostViewHolder;
import com.onrushers.domain.business.model.IBoost;

import java.util.ArrayList;
import java.util.List;

public class FeedBoostsAdapter extends RecyclerView.Adapter<FeedBoostViewHolder> {

	private List<IBoost> mBoosts;


	public void appendBoosts(List<IBoost> boosts, int page) {
		if (mBoosts == null) {
			mBoosts = new ArrayList<>();
		}
		if (page == 1) {
			mBoosts.clear();
		}
		mBoosts.addAll(boosts);
		notifyDataSetChanged();
	}

	@Override
	public FeedBoostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View itemView = layoutInflater.inflate(R.layout.card_feed_boost, parent, false);
		return new FeedBoostViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(FeedBoostViewHolder holder, int position) {
		IBoost boost = mBoosts.get(position);
		holder.setBoost(boost);
	}

	@Override
	public int getItemCount() {
		if (mBoosts != null) {
			return mBoosts.size();
		}
		return 0;
	}
}
