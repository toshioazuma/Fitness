package com.onrushers.app.user.picker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onrushers.app.R;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.ISearchResult;
import com.onrushers.domain.business.model.IUser;

import java.util.List;

public class UserPickerAdapter extends RecyclerView.Adapter<UserPickerItemViewHolder> {

	private List<ISearchResult> mResultsList;
	private List<IUser> mSelectedResultsList;

	private final UserPickerSelectionListener mSelectionListener;
	private final FileClient                  mFileClient;

	public UserPickerAdapter(UserPickerSelectionListener selectionListener, FileClient fileClient) {
		mSelectionListener = selectionListener;
		mFileClient = fileClient;
	}

	public void setUsersList(List<ISearchResult> resultsList, List<IUser> selectedList) {
		mResultsList = resultsList;
		mSelectedResultsList = selectedList;
		notifyDataSetChanged();
	}

	@Override
	public UserPickerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View itemView = inflater.inflate(R.layout.card_search_user_selection_row, parent, false);

		return new UserPickerItemViewHolder(itemView, mSelectionListener, mFileClient);
	}

	@Override
	public void onBindViewHolder(UserPickerItemViewHolder holder, int position) {
		ISearchResult userResult = mResultsList.get(position);
		holder.setUserResult(userResult);

		if (mSelectedResultsList != null && mSelectedResultsList.contains(userResult)) {
			holder.setSelected(true);
		} else {
			holder.setSelected(false);
		}
	}

	@Override
	public int getItemCount() {
		if (mResultsList != null) {
			return mResultsList.size();
		}
		return 0;
	}
}
