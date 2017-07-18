package com.onrushers.app.user.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.MainComponent;
import com.onrushers.app.user.list.adapter.UserListAdapter;
import com.onrushers.domain.business.interactor.boost.GetFeedBoostsInteractor;
import com.onrushers.domain.business.interactor.user.GetUserFansInteractor;
import com.onrushers.domain.business.interactor.user.GetUserHerosInteractor;
import com.onrushers.domain.business.interactor.user.GetUsersInteractor;
import com.onrushers.domain.business.model.IUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserListFragment extends BaseFragment implements UserListView, AbsListView.OnScrollListener {

	@Bind(R.id.user_listview)
	ListView mListView;

	@Bind(R.id.user_list_progressbar)
	ProgressBar mProgressBar;

	//region Dependencies injection
	//----------------------------------------------------------------------------------------------

	@Inject
	FileClient mFileClient;

	@Inject
	GetUsersInteractor mGetUsersInteractor;

	@Inject
	GetUserFansInteractor mGetUserFansInteractor;

	@Inject
	GetUserHerosInteractor mGetUserHerosInteractor;

	@Inject
	GetFeedBoostsInteractor mGetFeedBoostsInteractor;

	//----------------------------------------------------------------------------------------------
	//endregion

	UserListPresenterFactory mPresenterFactory;

	UserListPresenter mPresenter;

	UserListAdapter mAdatpter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(MainComponent.class).inject(this);

		mPresenterFactory = new UserListPresenterFactory(mGetUsersInteractor,
			mGetUserFansInteractor, mGetUserHerosInteractor, mGetFeedBoostsInteractor);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_list, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mAdatpter = new UserListAdapter(getContext());
		mListView.setAdapter(mAdatpter);
		mListView.setOnScrollListener(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserListView
	//----------------------------------------------------------------------------------------------

	@Override
	public void setTitle(int titleResId) {
		getActivity().setTitle(titleResId);
	}

	@Override
	public void setConfiguration(UserListConfiguration configuration) {
		mPresenter = mPresenterFactory.getPresenter(configuration);
		mPresenter.setView(this);
		mPresenter.fetchContextListAtPage(1);
	}

	@Override public void showLoading() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	@Override public void hideLoading() {
		mProgressBar.setVisibility(View.GONE);
	}

	@Override public void showUsers(@NotNull List<? extends IUser> users, int page) {
		mAdatpter.appendUsers(users, page);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region AbsListView.OnScrollListener
	//----------------------------------------------------------------------------------------------

	@Override public void onScrollStateChanged(AbsListView absListView, int i) {

	}

	@Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (totalItemCount - (firstVisibleItem + 1 + visibleItemCount) < 2 && visibleItemCount < totalItemCount) {
			mPresenter.fetchNextContextListPage();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

}
