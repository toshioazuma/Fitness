package com.onrushers.app.internal.di.components;

import com.onrushers.app.event.detail.EventDetailFragment;
import com.onrushers.app.event.home.EventsFragment;
import com.onrushers.app.event.register.EventRegisterFragment;
import com.onrushers.app.event.ticket.EventTicketFragment;
import com.onrushers.app.internal.di.PerActivity;
import com.onrushers.app.internal.di.modules.ActivityModule;
import com.onrushers.app.internal.di.modules.EventModule;
import com.onrushers.app.picture.gallery.PicturePageFragment;
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
		EventModule.class
	}
)
public interface EventComponent extends ActivityComponent {

	void inject(EventsFragment eventsFragment);

	void inject(EventDetailFragment eventDetailFragment);

	void inject(EventRegisterFragment eventRegisterFragment);

	void inject(EventTicketFragment eventTicketFragment);

	void inject(PicturePageFragment picturePageFragment);
}
