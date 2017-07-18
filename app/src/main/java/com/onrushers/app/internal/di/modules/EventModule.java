package com.onrushers.app.internal.di.modules;

import com.onrushers.app.event.detail.EventDetailPresenter;
import com.onrushers.app.event.detail.impl.EventDetailPresenterImpl;
import com.onrushers.app.event.home.EventsPresenter;
import com.onrushers.app.event.home.impl.EventsPresenterImpl;
import com.onrushers.app.event.register.EventRegisterPresenter;
import com.onrushers.app.event.register.impl.EventRegisterPresenterImpl;
import com.onrushers.app.event.ticket.EventTicketPresenter;
import com.onrushers.app.event.ticket.impl.EventTicketPresenterImpl;
import com.onrushers.app.file.FileClient;
import com.onrushers.app.file.impl.FileClientImpl;
import com.onrushers.app.internal.di.PerActivity;

import java.text.SimpleDateFormat;

import dagger.Module;
import dagger.Provides;

@Module
public class EventModule {

	public EventModule() {

	}

	@Provides
	@PerActivity
	public EventsPresenter provideEventsPresenter(EventsPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public EventDetailPresenter provideEventDetailPresenter(EventDetailPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public EventRegisterPresenter provideEventRegisterPresenter(EventRegisterPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	public EventTicketPresenter provideEventTicketPresenter(EventTicketPresenterImpl presenter) {
		return presenter;
	}

	@Provides
	@PerActivity
	FileClient provideFileClient(FileClientImpl fileClient) {
		return fileClient;
	}

	@Provides
	@PerActivity
	SimpleDateFormat provideDateFormat() {
		return new SimpleDateFormat();
	}
}
