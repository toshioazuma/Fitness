package com.onrushers.domain.usecases;

import android.os.Handler;
import android.os.HandlerThread;

import com.onrushers.domain.business.interactor.Interactor;
import com.onrushers.domain.executor.PostExecutionThread;
import com.onrushers.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.HandlerScheduler;
import rx.subscriptions.Subscriptions;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public abstract class UseCase implements Interactor {

	private final ThreadExecutor      mThreadExecutor;
	private final PostExecutionThread mPostExecutionThread;

	private Subscription mSubscription = Subscriptions.empty();

	protected UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
		mThreadExecutor = threadExecutor;
		mPostExecutionThread = postExecutionThread;
	}

	protected abstract Observable buildUseCaseObservable();

	@Override
	public void execute(Subscriber useCaseSubscriber) {
		HandlerThread bgThread = new HandlerThread("bgThread", THREAD_PRIORITY_BACKGROUND);
		bgThread.start();

		Handler bgHandler = new Handler(bgThread.getLooper());

		mSubscription = buildUseCaseObservable()
				.subscribeOn(HandlerScheduler.from(bgHandler))
				.observeOn(mPostExecutionThread.getScheduler())
				.subscribe(useCaseSubscriber);
	}

	@Override
	public void unsubscribe() {
		if (!mSubscription.isUnsubscribed()) {
			mSubscription.unsubscribe();
		}
	}

}
