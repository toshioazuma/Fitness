package com.onrushers.data.api.service;

import com.onrushers.data.models.UploadResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface FileService {

	@POST("api/upload")
	@Multipart
	Observable<UploadResult> uploadFile(@Part MultipartBody.Part file,
	                                    @Query("access_token") String accessToken);

	@GET("api/download")
	Call<String> downloadFile(@Query("id") int fileId,
	                          @Query("access_token") String accessToken);

}
