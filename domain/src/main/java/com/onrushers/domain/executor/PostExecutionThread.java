package com.onrushers.domain.executor;

import rx.Scheduler;

public interface PostExecutionThread {

	Scheduler getScheduler();

}
