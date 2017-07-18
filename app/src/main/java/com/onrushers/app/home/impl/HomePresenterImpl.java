package com.onrushers.app.home.impl;

import com.onrushers.app.feed.impl.FeedsListPresenterImpl;
import com.onrushers.app.home.HomePresenter;
import com.onrushers.app.home.HomeView;
import com.onrushers.domain.business.interactor.boost.AddBoostInteractor;
import com.onrushers.domain.business.interactor.boost.DeleteBoostInteractor;
import com.onrushers.domain.business.interactor.feed.DeleteFeedInteractor;
import com.onrushers.domain.business.interactor.other.GetSliderInteractor;
import com.onrushers.domain.business.interactor.relation.CreateRelationInteractor;
import com.onrushers.domain.business.interactor.report.CreateReportFeedInteractor;
import com.onrushers.domain.business.interactor.user.GetUserWallInteractor;
import com.onrushers.domain.business.model.IFeed;
import com.onrushers.domain.business.model.IRelation;
import com.onrushers.domain.business.model.ISliderResult;
import com.onrushers.domain.common.DefaultSubscriber;

import javax.inject.Inject;

public class HomePresenterImpl extends FeedsListPresenterImpl implements HomePresenter {

	private static final String TAG = "HomePresenterImpl";

	private final GetUserWallInteractor    mGetUserWallInteractor;
	private final GetSliderInteractor      mGetSliderInteractor;
	private final CreateRelationInteractor mCreateRelationInteractor;

	private HomeView mView;

	@Inject
	public HomePresenterImpl(GetUserWallInteractor getUserWallInteractor,
	                         DeleteFeedInteractor deleteFeedInteractor,
	                         CreateReportFeedInteractor createReportFeedInteractor,
	                         AddBoostInteractor addBoostInteractor,
	                         DeleteBoostInteractor deleteBoostInteractor,
	                         GetSliderInteractor getSliderInteractor,
	                         CreateRelationInteractor createRelationInteractor) {

		super(deleteFeedInteractor, createReportFeedInteractor, addBoostInteractor, deleteBoostInteractor);

		mGetUserWallInteractor = getUserWallInteractor;
		mGetUserWallInteractor.setUserId(null);

		mGetSliderInteractor = getSliderInteractor;
		mCreateRelationInteractor = createRelationInteractor;
	}

	@Override
	protected void onGetFeedsPage(int page) {
		mGetUserWallInteractor.setPage(page);
		mGetUserWallInteractor.execute(new GetFeedsSubscriber());
	}

	//region HomePresenter
	//----------------------------------------------------------------------------------------------

	@Override
	public void setView(HomeView view) {
		super.setView(view);
		mView = view;
	}

	@Override
	public void getPhotosSlider() {
		mGetSliderInteractor.execute(new DefaultSubscriber<ISliderResult>() {

			@Override
			public void onError(Throwable e) {
				super.onError(e);
				mView.hideTopSlider();
			}

			@Override
			public void onNext(ISliderResult sliderResult) {
				if (sliderResult.getPhotos() != null && !sliderResult.getPhotos().isEmpty()) {
					mView.showTopSlider(sliderResult.getPhotos());
				} else {
					mView.hideTopSlider();
				}
			}
		});
	}

	@Override
	public void followUser(IFeed feed) {
		if (feed.getOwner() == null) {
			return;
		}
		mCreateRelationInteractor.setHero(feed.getOwner());
		mCreateRelationInteractor.execute(new DefaultSubscriber());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mGetUserWallInteractor.unsubscribe();
		mGetSliderInteractor.unsubscribe();
		mCreateRelationInteractor.unsubscribe();
		mView = null;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
