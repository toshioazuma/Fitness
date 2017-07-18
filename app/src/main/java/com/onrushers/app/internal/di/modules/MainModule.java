package com.onrushers.app.internal.di.modules;

import com.onrushers.app.event.home.EventsPresenter;
import com.onrushers.app.event.home.impl.EventsPresenterImpl;
import com.onrushers.app.explore.ExplorePresenter;
import com.onrushers.app.explore.impl.ExplorePresenterImpl;
import com.onrushers.app.explore.tabs.photos.ExploreTabPhotosPresenter;
import com.onrushers.app.explore.tabs.photos.impl.ExploreTabPhotosPresenterImpl;
import com.onrushers.app.explore.tabs.ranking.ExploreTabRankingPresenter;
import com.onrushers.app.explore.tabs.ranking.impl.ExploreTabRankingPresenterImpl;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.file.impl.FileClientImpl;
import com.onrushers.app.home.HomePresenter;
import com.onrushers.app.home.impl.HomePresenterImpl;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.menu.MenuPresenter;
import com.onrushers.app.menu.impl.MenuPresenterImpl;
import com.onrushers.app.notification.NotificationsPresenter;
import com.onrushers.app.notification.impl.NotificationsPresenterImpl;
import com.onrushers.app.settings.SettingsPresenter;
import com.onrushers.app.settings.edit_account.EditAccountPresenter;
import com.onrushers.app.settings.edit_account.impl.EditAccountPresenterImpl;
import com.onrushers.app.settings.impl.SettingsPresenterImpl;
import com.onrushers.app.settings.social_account.SocialAccountPresenter;
import com.onrushers.app.settings.social_account.impl.SocialAccountPresenterImpl;
import com.onrushers.app.user.UserProfilePresenter;
import com.onrushers.app.user.impl.UserProfilePresenterImpl;
import com.onrushers.app.user.tabs.feeds.UserTabFeedsPresenter;
import com.onrushers.app.user.tabs.feeds.impl.UserTabFeedsPresenterImpl;
import com.onrushers.app.user.tabs.photos.UserTabPhotosPresenter;
import com.onrushers.app.user.tabs.photos.impl.UserTabPhotosPresenterImpl;
import com.onrushers.app.user.tabs.training.UserTabTrainingPresenter;
import com.onrushers.app.user.tabs.training.impl.UserTabTrainingPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

	public MainModule() {

	}

	@Provides
	@PerActivity
	public MenuPresenter provideMenuPresenter(MenuPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public HomePresenter provideHomePresenter(HomePresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public NotificationsPresenter provideNotificationsPresenter(NotificationsPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	public UserTabFeedsPresenter provideUserTabFeedsPresenter(UserTabFeedsPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	public UserTabPhotosPresenter provideUserTabPhotosPresenter(UserTabPhotosPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	public UserTabTrainingPresenter providerUserTabTrainingPresenter(UserTabTrainingPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	public UserProfilePresenter provideUserProfilePresenter(UserProfilePresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public EventsPresenter provideEventsPresenter(EventsPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public SettingsPresenter provideSettingsPresenter(SettingsPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public EditAccountPresenter provideEditAccountPresenter(EditAccountPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public SocialAccountPresenter provideSocialAccountPresenter(SocialAccountPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public ExplorePresenter provideExplorePresenter(ExplorePresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public ExploreTabPhotosPresenter provideExploreTabPhotosPresenter(ExploreTabPhotosPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public ExploreTabRankingPresenter provideExploreTabRankingPresenter(ExploreTabRankingPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity FileClient provideFileClient(FileClientImpl fileClient) {
		return fileClient;
	}
}
