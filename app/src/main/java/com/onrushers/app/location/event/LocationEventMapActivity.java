package com.onrushers.app.location.event;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onrushers.app.R;
import com.onrushers.app.common.Constant;
import com.onrushers.app.common.Extra;
import com.onrushers.domain.business.model.IEvent;
import com.onrushers.domain.business.model.ILocation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocationEventMapActivity extends FragmentActivity implements OnMapReadyCallback {

	private static final String TAG = "LocationEventMapA";

	@Bind(R.id.location_event_toolbar)
	Toolbar mToolbar;

	private IEvent mEvent;

	//region Activity life cycle
	//----------------------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_event_map);
		ButterKnife.bind(this);

		mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		if (getIntent() != null && getIntent().hasExtra(Extra.EVENT)) {
			mEvent = getIntent().getParcelableExtra(Extra.EVENT);
			mToolbar.setTitle(mEvent.getTitle());
		}

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.location_event_map);
		mapFragment.getMapAsync(this);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region OnMapReadyCallback
	//----------------------------------------------------------------------------------------------

	@Override
	public void onMapReady(GoogleMap googleMap) {

		if (mEvent != null && mEvent.getLocation() != null) {
			ILocation location = mEvent.getLocation();

			MarkerOptions markerOptions = new MarkerOptions()
				.position(new LatLng(location.getLatitude(), location.getLongitude()))
				.title(mEvent.getTitle())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker_black))
				.draggable(false);

			googleMap.addMarker(markerOptions);

			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory
				.newLatLngZoom(latLng, Constant.MAP_USER_DEFAULT_ZOOM);

			googleMap.animateCamera(cameraUpdate);
		} else {
			Log.w(TAG, "Extra event or location event are null");
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
