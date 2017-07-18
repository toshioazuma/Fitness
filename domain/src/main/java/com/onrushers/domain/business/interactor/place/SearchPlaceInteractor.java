package com.onrushers.domain.business.interactor.place;

import com.onrushers.domain.business.interactor.Interactor;

public interface SearchPlaceInteractor extends Interactor {

	void setApiKey(String apiKey);

	void setSearchText(String searchText);

	void setType(String type);

	void setLocation(double latitude, double longitude);

	void setRadius(int radius);
}
