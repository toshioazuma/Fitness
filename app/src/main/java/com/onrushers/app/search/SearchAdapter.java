package com.onrushers.app.search;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.user.OnUserDetailListener;
import com.onrushers.domain.business.model.ISearchResult;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

	private static final int EVENT_ITEM_VIEW_TYPE = 1;
	private static final int USER_ITEM_VIEW_TYPE  = 2;

	private List<ISearchResult> mResultsList;

	private final OnEventDetailListener mEventListener;
	private final OnUserDetailListener  mUserListener;

	public SearchAdapter(OnEventDetailListener eventListener) {
		mEventListener = eventListener;
		mUserListener = null;
	}

	public SearchAdapter(OnUserDetailListener userListener) {
		mEventListener = null;
		mUserListener = userListener;
	}

	public void setResultsList(List<ISearchResult> resultsList) {
		mResultsList = resultsList;
		notifyDataSetChanged();
	}

	//region RecyclerView.Adapter
	//----------------------------------------------------------------------------------------------

	@Override
	public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == EVENT_ITEM_VIEW_TYPE) {
			return SearchResultViewHolder.newInstance(parent, mEventListener);
		} else {
			return SearchResultViewHolder.newInstance(parent, mUserListener);
		}
	}

	@Override
	public void onBindViewHolder(SearchResultViewHolder holder, int position) {
		holder.setResult(mResultsList.get(position));
	}

	@Override
	public int getItemViewType(int position) {
		if (mEventListener != null) {
			return EVENT_ITEM_VIEW_TYPE;
		}
		return USER_ITEM_VIEW_TYPE;
	}

	@Override
	public int getItemCount() {
		if (mResultsList != null) {
			return mResultsList.size();
		}
		return 0;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
