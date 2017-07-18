package com.onrushers.data.api.service;

import com.onrushers.data.models.GenericResult;
import com.onrushers.data.models.Relation;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RelationService {

	@POST("api/relations")
	Observable<Relation> postRelation(@Body Relation relation,
	                                  @Query("access_token") String accessToken);

	@DELETE("api/relations/{id}")
	Observable<GenericResult> deleteRelation(@Path("id") int relationId,
	                                         @Query("access_token") String accessToken);
}
