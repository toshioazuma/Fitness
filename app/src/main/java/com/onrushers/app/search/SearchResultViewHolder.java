package com.onrushers.app.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.lazy.LazyUser;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.lazy.LazyEvent;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	@Bind(R.id.card_search_result_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_search_result_name_textview)
	TextView mNameTextView;

	private final OnEventDetailListener mEventDetailListener;
	private final OnUserDetailListener  mUserDetailListener;

	private ISearchResult mResult;


	//region Event Search mode
	//----------------------------------------------------------------------------------------------

	public static final SearchResultViewHolder newInstance(ViewGroup parent, OnEventDetailListener detailListener) {

		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_search_result_item, parent, false);

		return new SearchResultViewHolder(itemView, detailListener);
	}

	private SearchResultViewHolder(View itemView, OnEventDetailListener detailListener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		itemView.setOnClickListener(this);
		mEventDetailListener = detailListener;
		mUserDetailListener = null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region User Search mode
	//----------------------------------------------------------------------------------------------

	public static final SearchResultViewHolder newInstance(ViewGroup parent, OnUserDetailListener detailListener) {

		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_search_result_item, parent, false);

		return new SearchResultViewHolder(itemView, detailListener);
	}

	private SearchResultViewHolder(View itemView, OnUserDetailListener detailListener) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		itemView.setOnClickListener(this);
		mEventDetailListener = null;
		mUserDetailListener = detailListener;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	public void setResult(ISearchResult result) {
		mResult = result;

		mNameTextView.setText(result.getName());
		mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

		if (result.getPhoto() != null) {
			String fileUrl = Downloader.Companion.getInstance().resourceUrl(result.getPhoto());

			Glide.with(mAvatarImageView.getContext())
				.load(fileUrl)
				.centerCrop()
				.into(mAvatarImageView);
		}
	}

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@Override
	public void onClick(View view) {
		if (view.equals(itemView)) {
			if (mEventDetailListener != null) {
				mEventDetailListener.onEventDetail(new LazyEvent(mResult.getId(), mResult.getName()));
			} else if (mUserDetailListener != null) {
				mUserDetailListener.onUserDetail(new LazyUser(mResult.getId()));
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
