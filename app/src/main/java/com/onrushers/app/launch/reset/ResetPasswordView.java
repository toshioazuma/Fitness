package com.onrushers.app.launch.reset;

public interface ResetPasswordView {

	void showEmailSentPage(String subject, boolean isEmail);

	void showUserNotFoundError();
}
