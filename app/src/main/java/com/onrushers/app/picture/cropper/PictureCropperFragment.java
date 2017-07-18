package com.onrushers.app.picture.cropper;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyft.android.scissors.CropView;
import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * <p>
 * This fragment handles the crop of the picture.
 * </p>
 * <p>
 * Set {@link PictureCropperFragment.Listener#onCropPicture(File)}
 * to get back the cropped portion of the original picture set with the
 * {@link PictureCropperFragment#ARG_PICTURE_PATH} argument option.
 * </p>
 * <p>
 * Use {@link PictureCropperFragment#newInstance(String, String)} to build fragment instance with
 * picture path.
 * </p>
 */
public class PictureCropperFragment extends BaseFragment {

	public static final String TAG = "PictureCropper";

	private static final String ARG_PICTURE_PATH = "picture_path";
	private static final String ARG_CROP_TYPE    = "crop_type";

	@Bind(R.id.picture_cropper_cropview)
	protected CropView mCropView;

	private String mPicturePath;
	private String mCropType;

	private Listener mListener;

	public PictureCropperFragment() {
		/**
		 * Required empty public constructor
		 */
	}

	public static final PictureCropperFragment newInstance(String picturePath, String cropType) {
		Bundle args = new Bundle();
		args.putString(ARG_PICTURE_PATH, picturePath);
		args.putString(ARG_CROP_TYPE, cropType);

		PictureCropperFragment fragment = new PictureCropperFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public void setListener(Listener listener) {
		mListener = listener;
	}

	//region Fragment life cycle
	//---------------------------------------------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mPicturePath = getArguments().getString(ARG_PICTURE_PATH);
			mCropType = getArguments().getString(ARG_CROP_TYPE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picture_cropper, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (mPicturePath != null) {
			/**
			 * Crop view configuration.
			 */
			mCropView.setImageURI(Uri.parse(mPicturePath));
		}

		if (PictureCropperMode.CIRCLE.equals(mCropType)) {
			mCropView.setOverlayShape(CropView.OverlayShape.CIRCLE);
			mCropView.setViewportRatio(1);
		} else if (PictureCropperMode.RECTANGLE.equals(mCropType)) {
			mCropView.setOverlayShape(CropView.OverlayShape.RECT);
			mCropView.setViewportRatio(1.3f);
		} else {
			mCropView.setOverlayShape(CropView.OverlayShape.SQUARE);
			mCropView.setViewportRatio(1);
		}
	}

	//---------------------------------------------------------------------------------------
	//endregion

	//region Buttons action
	//---------------------------------------------------------------------------------------

	@OnClick(R.id.picture_cropper_cancel_button)
	public void onCancelClick() {
		getActivity().onBackPressed();
	}

	@OnClick(R.id.picture_cropper_finished_button)
	public void onFinishedClick() {

		final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		final String filename = timestamp + ".jpg";

		final File croppedFile = new File(getContext().getCacheDir(), filename);

		Observable<Void> onSave = Observable.from(
			mCropView.extensions()
				.crop()
				.quality(50) // 100
				.format(Bitmap.CompressFormat.JPEG)
				.into(croppedFile))
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());

		onSave.subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				if (mListener != null) {
					mListener.onCropPicture(croppedFile);
				}
			}
		});
	}

	//---------------------------------------------------------------------------------------
	//endregion

	public interface Listener {
		void onCropPicture(final File croppedFile);
	}
}
