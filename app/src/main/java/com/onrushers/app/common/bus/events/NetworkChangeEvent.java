package com.onrushers.app.common.bus.events;

public class NetworkChangeEvent {

	private final boolean mAvailable;

	public NetworkChangeEvent(boolean available) {
		mAvailable = available;
	}

	public boolean isAvailable() {
		return mAvailable;
	}
}
