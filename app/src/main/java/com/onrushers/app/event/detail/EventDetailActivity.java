package com.onrushers.app.event.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.onrushers.app.R;
import com.onrushers.app.common.Extra;
import com.onrushers.app.common.activities.BaseActivity;
import com.onrushers.app.internal.di.HasComponent;
import com.onrushers.app.internal.di.components.DaggerEventComponent;
import com.onrushers.app.internal.di.components.EventComponent;
import com.onrushers.app.internal.di.modules.EventModule;
import com.onrushers.domain.business.model.IEvent;

public class EventDetailActivity extends BaseActivity implements HasComponent<EventComponent> {

	private EventComponent mComponent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		if (getIntent() != null && getIntent().hasExtra(Extra.EVENT)) {
			IEvent event = getIntent().getParcelableExtra(Extra.EVENT);
			replaceCurrentFragment(EventDetailFragment.newInstance(event), EventDetailFragment.TAG);
		}
	}

	@Override
	public void replaceCurrentFragment(Fragment fragment, String tag) {
		replaceFragment(R.id.event_detail_content_frame, fragment, tag);
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
}
