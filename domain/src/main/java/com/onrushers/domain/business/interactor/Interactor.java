package com.onrushers.domain.business.interactor;

import rx.Subscriber;

public interface Interactor {

	void execute(Subscriber useCaseSubscriber);

	void unsubscribe();
}
