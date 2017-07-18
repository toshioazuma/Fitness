package com.onrushers.app.event.home.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.event.detail.EventDetailActivity;
import com.onrushers.app.event.detail.OnEventDetailListener;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.domain.business.model.IEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class EventsTabBaseFragment extends BaseFragment implements OnEventDetailListener {

	@Bind(R.id.base_events_no_data_message_textview)
	protected TextView mNoDataMessageTextView;

	@Bind(R.id.base_events_list_recycler)
	protected RecyclerView mRecyclerView;

	protected EventsTabAdapter mAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events_tab_base, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter = new EventsTabAdapter(this, getChildFragmentManager());
		mRecyclerView.setAdapter(mAdapter);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnEventDetailListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onEventDetail(IEvent event) {
		Intent intent = new Intent(getActivity(), EventDetailActivity.class);
		intent.putExtra(Extra.EVENT, event);
		startActivity(intent);
		//getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	public abstract int getNoDataMessageResId(boolean isSearching);

	public void showEvents(List<IEvent> events, boolean isSearching) {
		if (events == null || events.isEmpty()) {
			mRecyclerView.setVisibility(View.GONE);
			mNoDataMessageTextView.setVisibility(View.VISIBLE);

			int noDataMessageResId = getNoDataMessageResId(isSearching);
			if (noDataMessageResId > 0) {
				mNoDataMessageTextView.setText(noDataMessageResId);
			}
		} else {
			mRecyclerView.setVisibility(View.VISIBLE);
			mNoDataMessageTextView.setVisibility(View.GONE);
			mAdapter.appendsEvents(events, 1);
		}
	}
}
