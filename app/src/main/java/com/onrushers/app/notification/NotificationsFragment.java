package com.onrushers.app.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.event.detail.EventDetailActivity;
import com.onrushers.app.feed.detail.FeedDetailActivity;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.domain.business.lazy.LazyEvent;
import com.onrushers.domain.business.lazy.LazyFeed;
import com.onrushers.domain.business.lazy.LazyUser;
import com.onrushers.domain.business.model.INotification;
import com.onrushers.domain.business.type.FeedType;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class NotificationsFragment extends BaseFragment implements NotificationsView, AdapterView.OnItemClickListener {

	public static final String TAG = "NotificationsF";

	@Bind(R.id.stub_header_textview)
	TextView mHeaderTextView;

	@Bind(R.id.notifications_empty_textview)
	TextView mEmptyTextView;

	@Bind(R.id.notifications_listview)
	ListView mListView;

	@Inject
	NotificationsPresenter mPresenter;

	NotificationsAdapter mAdapter;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notifications_list, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mHeaderTextView.setText(R.string.notifications_title);

		mAdapter = new NotificationsAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		mListView.setAdapter(null);

		mAdapter = null;
		mPresenter = null;

		super.onDestroy();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region NotificationsView
	//----------------------------------------------------------------------------------------------

	@Override public void showNotifications(@NotNull List<? extends INotification> notifications) {
		if (notifications.isEmpty()) {
			mListView.setVisibility(View.GONE);
			mEmptyTextView.setVisibility(View.VISIBLE);
		} else {
			mEmptyTextView.setVisibility(View.GONE);
			mAdapter.setNotifications(notifications);
			mListView.setVisibility(View.VISIBLE);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region AdapterView.OnItemClickListener
	//----------------------------------------------------------------------------------------------

	@Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		showNotification((INotification) mAdapter.getItem(i));
	}

	private void showNotification(INotification notification) {
		Intent intent = null;

		switch (notification.getNotificationType()) {
			case Comment:
			case Rushed:
				/** redirect to feed */
				intent = new Intent(getActivity(), FeedDetailActivity.class);
				intent.putExtra(Extra.FEED, new LazyFeed(FeedType.Post.getValue(), notification.getTypeId()));
				break;

			case Event:
				/** redirect to event */
				intent = new Intent(getActivity(), EventDetailActivity.class);
				intent.putExtra(Extra.EVENT, new LazyEvent(notification.getTypeId(), ""));
				break;

			case Fan:
				/** redirect to user */
				intent = new Intent(getActivity(), UserProfileActivity.class);
				intent.putExtra(Extra.USER, new LazyUser(notification.getTypeId()));
				break;

			default:
				break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
