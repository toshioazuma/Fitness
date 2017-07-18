package com.onrushers.domain.common;

/**
 * Stub rx.Subscriber class
 *
 * @param <T>
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {

	@Override
	public void onCompleted() {
		/**
		 * Empty implementation
		 */
	}

	@Override
	public void onError(Throwable e) {

		e.printStackTrace();
	}

	@Override
	public void onNext(T t) {
		/**
		 * Empty implementation
		 */
	}
}
