package com.onrushers.app.launch.launch;

public interface LaunchPresenter {

	void setView(LaunchView view);

	void onViewCreated();

	void onDestroy();


}
