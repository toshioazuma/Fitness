package com.onrushers.app.picture.picker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onrushers.app.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PicturePickerOptionsAdapter
		extends RecyclerView.Adapter<PicturePickerOptionsAdapter.ViewHolder> {

	private List<PicturePickerOptionItem>   mItems;
	private PicturePickerOptionItemListener mListener;

	public PicturePickerOptionsAdapter(
			List<PicturePickerOptionItem> items, PicturePickerOptionItemListener listener) {
		mItems = items;
		mListener = listener;
	}

	public void setListener(PicturePickerOptionItemListener listener) {
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.row_picture_picker_option_item, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.setItem(mItems.get(position));
	}

	@Override
	public int getItemCount() {
		if (mItems == null) {
			return 0;
		}
		return mItems.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@Bind(R.id.picture_picker_textview)
		protected TextView mTextView;

		protected PicturePickerOptionItem mItem;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		public void setItem(PicturePickerOptionItem item) {
			mItem = item;
			mTextView.setCompoundDrawablesWithIntrinsicBounds(item.getDrawableRes(), 0, 0, 0);
			mTextView.setText(item.getTitleRes());
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onPicturePickerItemOptionClick(mItem);
			}
		}
	}

	public interface PicturePickerOptionItemListener {
		void onPicturePickerItemOptionClick(PicturePickerOptionItem item);
	}
}
