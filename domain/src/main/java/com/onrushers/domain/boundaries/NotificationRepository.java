package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IPagination;

import rx.Observable;

public interface NotificationRepository {

	Observable<IPagination> getNotifications(int page, int count, String accessToken);
}
