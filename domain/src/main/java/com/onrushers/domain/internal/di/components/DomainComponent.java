package com.onrushers.domain.internal.di.components;

import com.onrushers.domain.internal.di.modules.DomainModule;

import dagger.Component;

@Component(
		modules = {
				DomainModule.class
		})
public interface DomainComponent {

	void inject(Object object);

}
