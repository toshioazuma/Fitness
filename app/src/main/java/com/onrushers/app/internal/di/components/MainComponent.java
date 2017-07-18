package com.onrushers.app.internal.di.components;

import com.onrushers.app.event.home.EventsFragment;
import com.onrushers.app.event.home.tabs.EventsTabBaseFragment;
import com.onrushers.app.explore.ExploreFragment;
import com.onrushers.app.explore.tabs.photos.ExploreTabPhotosFragment;
import com.onrushers.app.explore.tabs.ranking.ExploreTabRankingFragment;
import com.onrushers.app.home.HomeFragment;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.MainModule;
import com.onrushers.app.menu.MenuFragment;
import com.onrushers.app.notification.NotificationsFragment;
import com.onrushers.app.picture.gallery.PicturePageFragment;
import com.onrushers.app.settings.SettingsFragment;
import com.onrushers.app.settings.edit_account.EditAccountFragment;
import com.onrushers.app.settings.social_account.SocialAccountFragment;
import com.onrushers.app.user.UserProfileFragment;
import com.onrushers.app.user.list.UserListFragment;
import com.onrushers.app.user.tabs.feeds.UserTabFeedsFragment;
import com.onrushers.app.user.tabs.photos.UserTabPhotosFragment;
import com.onrushers.app.user.tabs.training.UserTabTrainingFragment;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.internal.di.modules.DomainModule;

import dagger.Component;

@PerActivity
@Component(
	dependencies = ApplicationComponent.class,
	modules = {
		ActivityModule.class,
		DomainModule.class,
		DataModule.class,
		MainModule.class
	}
)
public interface MainComponent extends ActivityComponent {

	void inject(MenuFragment menuFragment);

	void inject(HomeFragment homeFragment);

	void inject(NotificationsFragment notificationsFragment);

	void inject(UserProfileFragment userProfileFragment);

	void inject(UserTabFeedsFragment userTabFeedsFragment);

	void inject(UserTabPhotosFragment userTabPhotosFragment);

	void inject(UserTabTrainingFragment userTabTrainingFragment);

	void inject(UserListFragment userListFragment);

	void inject(EventsFragment eventsFragment);

	void inject(EventsTabBaseFragment eventsTabBaseFragment);

	void inject(ExploreFragment exploreFragment);

	void inject(ExploreTabPhotosFragment exploreTabPhotosFragment);

	void inject(ExploreTabRankingFragment exploreTabRankingFragment);

	void inject(SettingsFragment settingsFragment);

	void inject(EditAccountFragment editAccountFragment);

	void inject(SocialAccountFragment socialAccountFragment);

	void inject(PicturePageFragment picturePageFragment);
}
