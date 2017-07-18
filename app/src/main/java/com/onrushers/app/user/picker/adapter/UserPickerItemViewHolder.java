package com.onrushers.app.user.picker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.onrushers.app.R;
import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.model.ISearchResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserPickerItemViewHolder extends RecyclerView.ViewHolder {

	@Bind(R.id.card_search_user_avatar_imageview)
	CircularImageView mAvatarImageView;

	@Bind(R.id.card_search_user_name_textview)
	TextView mNameTextView;

	@Bind(R.id.card_search_user_add_button)
	Button mAddButton;


	private final Context    mContext;
	private final FileClient mFileClient;

	@NonNull
	private final UserPickerSelectionListener mSelectionListener;

	private ISearchResult mUserResult;

	public UserPickerItemViewHolder(View itemView, UserPickerSelectionListener selectionListener, FileClient fileClient) {
		super(itemView);
		ButterKnife.bind(this, itemView);

		mContext = itemView.getContext();
		mSelectionListener = selectionListener;
		mFileClient = fileClient;
	}

	public void setUserResult(ISearchResult userResult) {
		mUserResult = userResult;

		mNameTextView.setText(userResult.getName());
		mAvatarImageView.setImageResource(R.drawable.ic_user_avatar_default);

		if (userResult.getPhoto() != null) {
			mFileClient.getFile(userResult.getPhoto(), new FileClient.Receiver() {

				@Override
				public void onGetFileUrl(String fileUrl) {

					Glide.with(mAvatarImageView.getContext())
						.load(fileUrl)
						.centerCrop()
						.into(mAvatarImageView);
				}
			});
		}
	}

	public void setSelected(boolean selected) {
		mAddButton.setText(selected ? R.string.action_added : R.string.action_add);
		mAddButton.setSelected(selected);
	}

	//region OnClick
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.card_search_user_add_button)
	public void onAddUserClick() {
		if (mAddButton.isSelected()) {
			mAddButton.setText(R.string.action_add);
			mAddButton.setSelected(false);
			mSelectionListener.onDeselectUserResult(mUserResult);
		} else {
			mAddButton.setText(R.string.action_added);
			mAddButton.setSelected(true);
			mSelectionListener.onSelectUserResult(mUserResult);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
