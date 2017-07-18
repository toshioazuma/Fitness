package com.onrushers.app.launch.launch;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onrushers.app.R;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.LaunchComponent;
import com.onrushers.app.launch.home.LaunchHomeFragment;
import com.onrushers.common.defaults.DefaultAnimatorListener;
import com.onrushers.common.utils.DisplayUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchFragment extends BaseFragment implements LaunchView {

	@Bind(R.id.launch_logo_bird_imageview)
	ImageView mBirdImageView;

	@Bind(R.id.launch_logo_colored_imageview)
	ImageView mBrandImageView;

	@Inject
	LaunchPresenter mPresenter;

	private Callbacks mListener;

	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof Callbacks) {
			mListener = (Callbacks) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement LaunchFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(LaunchComponent.class).inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_launch, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPresenter.setView(this);

		mBrandImageView.setAlpha(0.0f);

		mBirdImageView.setScaleX(0);
		mBirdImageView.setScaleY(0);

		mPresenter.onViewCreated();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPresenter.onDestroy();
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region LaunchView
	//----------------------------------------------------------------------------------------------

	@Override
	public void showLaunchHome() {
		final Context appContext = getActivity().getApplicationContext();
		final float deltaBirdY = DisplayUtils.convertDpToPixel(appContext, -45);

		mBirdImageView.animate()
			.scaleX(1)
			.scaleY(1)
			.setDuration(3000)
			.setListener(new DefaultAnimatorListener() {

				@Override
				public void onAnimationEnd(Animator animator) {
					mBirdImageView.animate()
						.translationY(deltaBirdY)
						.setDuration(2500)
						.start();

					mBrandImageView.animate()
						.alpha(1)
						.setDuration(2000)
						.setStartDelay(1000)
						.start();
				}
			})
			.start();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				BaseActivity baseActivity = (BaseActivity) getActivity();
				baseActivity.replaceCurrentFragment(new LaunchHomeFragment(), LaunchHomeFragment.TAG);
			}
		}, 6100);
	}

	@Override
	public void showLoggedInHome() {

		if (mListener != null) {
			mListener.onAuthenticatedUser();
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Public callbacks
	//----------------------------------------------------------------------------------------------

	public interface Callbacks {

		void onAuthenticatedUser();
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
