package com.onrushers.app.internal.di.components;

import com.onrushers.app.feed.boost.FeedBoostListFragment;
import com.onrushers.app.feed.comment.FeedCommentFragment;
import com.onrushers.app.feed.create.FeedCreateFragment;
import com.onrushers.app.feed.detail.FeedDetailFragment;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.FeedModule;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.domain.internal.di.modules.DomainModule;

import dagger.Component;

@PerActivity
@Component(
	dependencies = ApplicationComponent.class,
	modules = {
		ActivityModule.class,
		FeedModule.class,
		DomainModule.class,
		DataModule.class
	}
)
public interface FeedComponent extends ActivityComponent {

	void inject(FeedCreateFragment feedCreateFragment);

    void inject(FeedDetailFragment feedDetailFragment);

	void inject(FeedCommentFragment feedCommentFragment);

	void inject(FeedBoostListFragment feedBoostListFragment);
}
