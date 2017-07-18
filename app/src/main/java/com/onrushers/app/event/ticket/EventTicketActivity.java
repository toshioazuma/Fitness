package com.onrushers.app.event.ticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerEventComponent;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.app.internal.di.modules.EventModule;
import com.onrushers.domain.business.model.IEvent;

public class EventTicketActivity extends BaseActivity implements HasComponent<EventComponent> {

	private EventComponent mComponent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_event_ticket);
		Toolbar toolbar = (Toolbar) findViewById(R.id.event_ticket_toolbar);
		setSupportActionBar(toolbar);

		if (getIntent() != null && getIntent().hasExtra(Extra.EVENT)) {
			IEvent event = getIntent().getParcelableExtra(Extra.EVENT);
			replaceCurrentFragment(EventTicketFragment.newInstance(event), EventTicketFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.event_ticket_content_frame, fragment, tag);
	}

	@Override
	public EventComponent getComponent() {
		if (mComponent == null) {
			mComponent = DaggerEventComponent.builder()
				.applicationComponent(getApplicationComponent())
				.activityModule(getActivityModule())
				.dataModule(getDataModule())
				.eventModule(new EventModule())
				.build();
		}
		return mComponent;
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
