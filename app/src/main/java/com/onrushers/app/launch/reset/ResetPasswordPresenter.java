package com.onrushers.app.launch.reset;

public interface ResetPasswordPresenter {

	void setView(ResetPasswordView view);

	void requestResetPassword(String username);
}
