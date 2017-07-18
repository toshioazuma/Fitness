package com.onrushers.domain.boundaries;

import com.onrushers.domain.business.model.IUploadResult;

import java.io.File;

import rx.Observable;

public interface FileRepository {

	Observable<IUploadResult> uploadFile(String mediaType, File file, String accessToken);

	Observable<String> getDownloadFile(Integer fileId, String accessToken);
}
