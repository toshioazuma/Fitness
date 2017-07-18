package com.onrushers.data.internal.di.components;

import com.onrushers.data.api.service.AuthenticationService;
import com.onrushers.data.api.service.FeedService;
import com.onrushers.data.internal.di.modules.DataModule;
import com.onrushers.data.api.service.UserService;
import com.onrushers.domain.boundaries.AuthenticationRepository;
import com.onrushers.domain.boundaries.UserRepository;
import com.onrushers.domain.internal.di.components.DomainComponent;

import dagger.Component;

@Component(
	dependencies = {
		DomainComponent.class
	},
	modules = {
		DataModule.class
	})
public interface DataComponent {

	void inject(Object object);

	AuthenticationRepository authenticationRepository();

	AuthenticationService authenticationService();

	UserRepository userRepository();

	UserService userService();

	FeedService feedService();

}
