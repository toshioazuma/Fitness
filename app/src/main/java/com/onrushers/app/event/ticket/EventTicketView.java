package com.onrushers.app.event.ticket;

public interface EventTicketView {

	void showTitle(String title);

	void showSubtitle(String subtitle);

	void showAvatar(String avatarUrl);

	void showPlaces(String places);

	void showPrice(String price);

	void showDate(String date, String time);

	void showLocation(String location);

	void showQrCode(String qrCodeUrl);
}
