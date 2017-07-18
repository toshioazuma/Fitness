package com.onrushers.app.user.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.UserComponent;
import com.onrushers.app.user.picker.adapter.UserPickerAdapter;
import com.onrushers.app.user.picker.adapter.UserPickerSelectionListener;
import com.onrushers.common.widgets.recyclerview.DividerItemDecoration;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPickerFragment extends BaseFragment
	implements UserPickerView, UserPickerSelectionListener {

	public static final String TAG = "UserPickerF";

	@Bind(R.id.user_picker_searchview)
	SearchView mSearchView;

	@Bind(R.id.user_picker_recyclerview)
	RecyclerView mPickerRecyclerView;

	@Inject
	UserPickerPresenter mPresenter;

	@Inject
	FileClient mFileClient;

	UserPickerAdapter mPickerAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(UserComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_picker, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mPickerAdapter = new UserPickerAdapter(this, mFileClient);
		mPickerRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
		mPickerRecyclerView.setAdapter(mPickerAdapter);

		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				mPresenter.searchQuery(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				mPresenter.searchQuery(newText);
				return true;
			}
		});
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserPickerView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showUsersList(List<ISearchResult> usersResultList) {
		mPickerAdapter.setUsersList(usersResultList, mPresenter.getSelectedUsers());
	}

	@Override
	public ArrayList<IUser> getSelectedUsers() {
		return mPresenter.getSelectedUsers();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region UserPickerSelectionListener
	//----------------------------------------------------------------------------------------------

	@Override
	public void onSelectUserResult(ISearchResult userResult) {
		mPresenter.addSelectedUser(userResult);
	}

	@Override
	public void onDeselectUserResult(ISearchResult userResult) {
		mPresenter.removeSelectedUser(userResult);
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
