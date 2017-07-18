package com.onrushers.app.feed.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.onrushers.app.BuildConfig;
import com.onrushers.app.R;
import com.onrushers.domain.business.model.IFile;

import java.util.List;

public class FeedImagesAdapter extends PagerAdapter {

	private Context        mContext;
	private LayoutInflater mLayoutInflater;

	private List<IFile> mImages;


	public FeedImagesAdapter(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}

	public void setImages(List<IFile> images) {
		mImages = images;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mImages != null) {
			return mImages.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = (ImageView)
			mLayoutInflater.inflate(R.layout.pager_item_feed_image, null, false);

		String filePath = mImages.get(position).getPath();
		String fullFilePath = BuildConfig.BASE_API + BuildConfig.UPLOAD_PATH + filePath;

		Glide.with(imageView.getContext())
			.load(fullFilePath)
			.asBitmap()
			.fitCenter()
			.into(imageView);

		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
