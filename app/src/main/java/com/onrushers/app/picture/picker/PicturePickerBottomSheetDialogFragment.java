package com.onrushers.app.picture.picker;

import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.onrushers.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PicturePickerBottomSheetDialogFragment extends BottomSheetDialogFragment
		implements PicturePickerOptionsAdapter.PicturePickerOptionItemListener {

	public static final String TAG = "PicturePickerBottomSheet";

	private static final int PICKER_OPTION_DISMISS      = 0;
	private static final int PICKER_OPTION_NEW_PICTURE  = 1;
	private static final int PICKER_OPTION_FROM_LIBRARY = 2;

	@Bind(R.id.bottom_sheet_dialog_picture_picker_recycler)
	protected RecyclerView mRecyclerView;

	private PicturePickerOptionsAdapter mAdapter;

	private ItemListener mListener;

	@Override
	public void setupDialog(Dialog dialog, int style) {
		super.setupDialog(dialog, style);

		View contentView = View.inflate(getContext(),
				R.layout.bottom_sheet_dialog_picture_picker, null);
		ButterKnife.bind(this, contentView);
		dialog.setContentView(contentView);

		mAdapter = new PicturePickerOptionsAdapter(getOptionItems(), this);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setNestedScrollingEnabled(false);

		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAdapter.setListener(null);
	}

	private final List<PicturePickerOptionItem> getOptionItems() {
		List<PicturePickerOptionItem> list = new ArrayList<>();
		list.add(new PicturePickerOptionItem(PICKER_OPTION_NEW_PICTURE, R.drawable.ic_camera,
				R.string.picker_picture_options_new_picture));
		list.add(new PicturePickerOptionItem(PICKER_OPTION_FROM_LIBRARY, R.drawable.ic_gallery,
				R.string.picker_picture_options_import_library));
		list.add(new PicturePickerOptionItem(PICKER_OPTION_DISMISS, R.drawable.ic_empty,
				R.string.picker_picture_options_dismiss));
		return list;
	}

	@Override
	public void onPicturePickerItemOptionClick(PicturePickerOptionItem item) {
		if (mListener != null) {
			switch (item.getId()) {
				case PICKER_OPTION_DISMISS:
					mListener.onPickerDismissClick(getDialog());
					break;
				case PICKER_OPTION_NEW_PICTURE:
					mListener.onPickerNewPictureClick(getDialog());
					break;
				case PICKER_OPTION_FROM_LIBRARY:
					mListener.onPickerFromLibraryClick(getDialog());
					break;
				default:
					break;
			}
		}
	}

	public void setListener(ItemListener listener) {
		mListener = listener;
	}

	public interface ItemListener {

		void onPickerDismissClick(Dialog dialog);

		void onPickerNewPictureClick(Dialog dialog);

		void onPickerFromLibraryClick(Dialog dialog);
	}
}