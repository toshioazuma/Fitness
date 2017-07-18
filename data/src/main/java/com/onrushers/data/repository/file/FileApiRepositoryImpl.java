package com.onrushers.data.repository.file;

import com.onrushers.data.api.service.FileService;
import com.onrushers.domain.boundaries.FileRepository;
import com.onrushers.domain.business.model.IUploadResult;
import com.onrushers.domain.business.model.IUploadedFile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class FileApiRepositoryImpl implements FileRepository {

	private final FileService mFileService;

	@Inject
	public FileApiRepositoryImpl(FileService fileService) {
		mFileService = fileService;
	}

	@Override
	public Observable<IUploadResult> uploadFile(String mediaType, File file, String accessToken) {

		final RequestBody requestFile = RequestBody.create(MediaType.parse(mediaType), file);

		MultipartBody.Part body =
				MultipartBody.Part.createFormData("file", file.getName(), requestFile);

		return mFileService.uploadFile(body, accessToken).cast(IUploadResult.class);
	}

	@Override
	public Observable<String> getDownloadFile(final Integer fileId, final String accessToken) {

		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {

				subscriber.onNext(
					mFileService.downloadFile(fileId, accessToken).request().url().toString());
			}
		});
	}
}
