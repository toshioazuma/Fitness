package com.onrushers.app.feed.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.feed.adapters.OnFeedHeaderListener;
import com.onrushers.app.feed.adapters.OnFeedRushListener;
import com.onrushers.app.feed.adapters.OnFeedRushesViewListener;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.app.user.UserProfileActivity;
import com.onrushers.app.user.list.UserListActivity;
import com.onrushers.app.user.list.UserListConfiguration;
import com.onrushers.common.utils.KeyboardUtils;
import com.onrushers.common.utils.ToastUtils;
import com.onrushers.common.widgets.recyclerview.EndlessRecyclerViewScrollListener;
import com.onrushers.domain.business.model.IBoost;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedDetailFragment extends BaseFragment implements FeedDetailView,
	OnFeedHeaderListener, OnCommentDeleteListener, OnFeedRushListener, OnFeedRushesViewListener {

	public static final String TAG = "FeedDetailF";

	@Bind(R.id.feed_detail_add_comment_button)
	ImageButton mAddCommentButton;

	@Bind(R.id.feed_detail_comment_input)
	EditText mCommentInput;

	@Bind(R.id.feed_detail_recycler)
	RecyclerView mRecyclerView;

	@Inject
	FeedDetailPresenter mPresenter;

	FeedDetailAdapter mAdapter;

	IFeed mFeed;

	OnFeedDetailReadyListener mReadyListener;


	//region Constructor
	//----------------------------------------------------------------------------------------------

	public static final FeedDetailFragment newInstance(IFeed feed) {

		Bundle args = new Bundle();
		args.putParcelable(Extra.FEED, feed);

		FeedDetailFragment fragment = new FeedDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(FeedComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_detail, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (getArguments().get(Extra.FEED) == null) {
			return;
		}

		mFeed = getArguments().getParcelable(Extra.FEED);
		mPresenter.setFeed(mFeed);

		mAdapter = new FeedDetailAdapter(mPresenter.getViewType(), mFeed);
		mAdapter.setOnFeedHeaderListener(this);
		mAdapter.setOnCommentDeleteListener(this);
		mAdapter.setOnFeedRushListener(this);
		mAdapter.setOnFeedRushesViewListener(this);
		mAdapter.setOnReadyListener(mReadyListener);
		mRecyclerView.setAdapter(mAdapter);

		LinearLayoutManager linearLayoutManager =
			(LinearLayoutManager) mRecyclerView.getLayoutManager();

		mRecyclerView.addOnScrollListener(
			new EndlessRecyclerViewScrollListener(linearLayoutManager, 5) {

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					mPresenter.getCommentsAtPage(page);
				}
			});
	}

	@Override
	public void onResume() {
		super.onResume();
		KeyboardUtils.hide(getActivity());
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region FeedDetailView
	//----------------------------------------------------------------------------------------------

	@Override public void setOnFeedDetailReadyListener(OnFeedDetailReadyListener readyListener) {
		mReadyListener = readyListener;
	}

	@Override public void updateFeed(IFeed feed) {
		mAdapter.setViewType(mPresenter.getViewType(), feed);
	}

	@Override
	public void showOptions(View anchor) {
		PopupMenu popup = new PopupMenu(getContext(), anchor);

		int menuRes;
		if (mFeed.isMine()) {
			menuRes = R.menu.card_feed_detail_my_options;
		} else {
			menuRes = R.menu.card_feed_detail_other_options;
		}
		popup.getMenuInflater().inflate(menuRes, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {
					case R.id.menu_feed_detail_report:
						mPresenter.reportFeed();
						return true;

					case R.id.menu_feed_detail_delete:
						mPresenter.deleteFeed();
						return true;
				}
				return false;
			}
		});
		popup.show();
	}

	@Override
	public void showReportSent() {
		ToastUtils.showText(getContext(), R.string.report_sent);
	}

	@Override
	public void showReportFailed() {
		ToastUtils.showText(getContext(), R.string.report_error);
	}

	@Override
	public void showComments(List<IComment> comments, int page) {
		mAdapter.addComments(comments, page);
	}

	@Override
	public void showCommentUsers(List<IUser> commentUsers) {
		mAdapter.addUsers(commentUsers);
	}

	@Override
	public void showCommentSent() {
		KeyboardUtils.hide(getActivity());

		mCommentInput.setText("");
		mAddCommentButton.setEnabled(true);
		mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
	}

	@Override public void showCommentDeleteFailed() {
		ToastUtils.showText(getContext(), R.string.feed_detail_comment_error_on_delete);
	}

	@Override public void showCommentDeleted() {
		ToastUtils.showText(getContext(), R.string.feed_detail_comment_deleted);
	}

	@Override public void showMessage(int messageId) {
		ToastUtils.showText(getContext(), messageId);
	}

	@Override public void reloadView() {
		mAdapter.notifyDataSetChanged();
	}

	@Override public void dismissView() {
		getActivity().finish();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedHeaderListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onShowUserProfilePage(IUser user) {
		Intent intent = new Intent(getActivity(), UserProfileActivity.class);
		intent.putExtra(Extra.USER, user);
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnCommentDeleteListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onDeleteComment(IComment comment) {
		mPresenter.deleteComment(comment);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedRushListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onRushFeed(IFeed feed) {
		mPresenter.addRush(feed);
	}

	@Override
	public void onUnrushFeed(IFeed feed, IBoost boost) {
		mPresenter.removeRush(feed, boost);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnFeedRushesViewListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onViewRushesFeed(IFeed feed) {
		Intent intent = new Intent(getActivity(), UserListActivity.class);
		intent.putExtra(Extra.USER_LIST_CONFIGURATION, UserListConfiguration.getRushesFeedConfiguration(feed));
		startActivity(intent);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.feed_detail_add_comment_button)
	public void onAddCommentClick() {
		String text = mCommentInput.getText().toString();
		if (TextUtils.isEmpty(text)) {
			return;
		}

		mAddCommentButton.setEnabled(false);
		mPresenter.addComment(text);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
