package com.onrushers.data.api.service;

import com.onrushers.data.models.Comment;
import com.onrushers.data.models.GenericResult;
import com.onrushers.data.models.Pagination;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface CommentService {

    @POST("api/comments")
    Observable<Comment> postComment(@Body Comment comment,
                                    @Query("access_token") String accessToken);

    @DELETE("api/comments/{id}")
    Observable<GenericResult> deleteComment(@Path("id") int commentId,
                                            @Query("access_token") String accessToken);

    @GET("api/feeds/{id}/comments")
    Observable<Pagination<Comment>> getFeedComments(@Path("id") int feedId,
                                                    @Query("page") int page,
                                                    @Query("count") int count,
                                                    @Query("access_token") String accessToken);
}
