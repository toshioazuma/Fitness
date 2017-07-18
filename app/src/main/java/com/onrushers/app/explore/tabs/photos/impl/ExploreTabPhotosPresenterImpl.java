package com.onrushers.app.explore.tabs.photos.impl;

import com.onrushers.app.common.Constant;
import com.onrushers.app.explore.tabs.photos.ExploreTabPhotosPresenter;
import com.onrushers.app.explore.tabs.photos.ExploreTabPhotosView;
import com.onrushers.domain.business.interactor.explore.GetExplorePhotosInteractor;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IPagination;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.List;

import javax.inject.Inject;

public class ExploreTabPhotosPresenterImpl implements ExploreTabPhotosPresenter {

	private ExploreTabPhotosView mView;
	private int mPage = 1;


	private final GetExplorePhotosInteractor mPhotosInteractor;

	@Inject
	public ExploreTabPhotosPresenterImpl(GetExplorePhotosInteractor photosInteractor) {
		mPhotosInteractor = photosInteractor;
		mPhotosInteractor.setCount(7 * Constant.GRID_PHOTO_COLUMN_COUNT);
	}

	@Override
	public void setView(ExploreTabPhotosView view) {
		mView = view;
	}

	@Override
	public void onViewCreated() {
		getPhotosAtPage(1);
	}

	@Override
	public void getPhotosAtPage(int page) {
		mPage = page;
		mPhotosInteractor.setPage(mPage);
		mPhotosInteractor.execute(new GetPhotosSubscriber());
	}

	@Override
	public void onDestroy() {
		mPhotosInteractor.unsubscribe();
	}

	//region Subscribers
	//----------------------------------------------------------------------------------------------

	private final class GetPhotosSubscriber extends DefaultSubscriber<IPagination<IFeed>> {

		@Override
		public void onNext(IPagination<IFeed> pagination) {
			List<IFeed> feedsList = pagination.getItems();
			if (feedsList == null || feedsList.isEmpty()) {
				return;
			}
			mView.showFeeds(feedsList, mPage);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
