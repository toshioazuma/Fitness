package com.onrushers.app.picture.fullscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFullscreenFragment extends BaseFragment {

	public static final String TAG = "PictureFullscreenF";

	@Bind(R.id.picture_fullscreen_toolbar)
	Toolbar mToolbar;

	@Bind(R.id.picture_fullscreen_imageview)
	ImageView mImageView;

	private String mPictureUrl;

	public static final PictureFullscreenFragment newInstance(String pictureUrl) {
		Bundle args = new Bundle();
		args.putString(Extra.PICTURE_URL, pictureUrl);

		PictureFullscreenFragment fragment = new PictureFullscreenFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null && getArguments().get(Extra.PICTURE_URL) != null) {
			mPictureUrl = getArguments().getString(Extra.PICTURE_URL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture_fullscreen, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		addNavigationBackToToolbar(mToolbar);

		if (mPictureUrl == null) {
			return;
		}

		Glide.with(this)
			.load(mPictureUrl)
			.crossFade()
			.placeholder(R.drawable.ic_user_avatar_default)
			.into(mImageView);
	}

	@Override
	protected void onNavigationBack() {
		getActivity().supportFinishAfterTransition();
	}
}
