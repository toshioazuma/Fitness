package com.onrushers.app.user.tabs.training;

public interface UserTabTrainingPresenter {

	void onViewCreated();

	void setView(UserTabTrainingView view);

	void setUserId(Integer userId);
}
