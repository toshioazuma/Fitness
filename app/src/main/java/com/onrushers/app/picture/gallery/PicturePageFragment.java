package com.onrushers.app.picture.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.OnRushersMainActivity;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.event.detail.EventDetailActivity;
import com.onrushers.app.file.Downloader;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.app.internal.di.components.MainComponent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicturePageFragment extends BaseFragment {

	@Bind(R.id.picture_page_imageview)
	ImageView mPictureImageView;

	@Inject
	FileClient mFileClient;

	public static final PicturePageFragment newInstance(int drawableId) {
		Bundle args = new Bundle();
		args.putInt(Extra.DRAWABLE_ID, drawableId);

		PicturePageFragment fragment = new PicturePageFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BaseActivity baseActivity = getBaseActivity();
		if (baseActivity instanceof OnRushersMainActivity) {
			getComponent(MainComponent.class).inject(this);
		} else if (baseActivity instanceof EventDetailActivity) {
			getComponent(EventComponent.class).inject(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture_page, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (getArguments() != null) {
			int drawableId = getArguments().getInt(Extra.DRAWABLE_ID, 0);

			if (drawableId == 0) {
				return;
			}

			mPictureImageView.setImageResource(R.drawable.ic_default_placeholder);

			String fileUrl = Downloader.Companion.getInstance().resourceUrl(drawableId);

			Glide.with(mPictureImageView.getContext())
				.load(fileUrl)
				.asBitmap()
				.centerCrop()
				.into(mPictureImageView);
		}
	}
}
