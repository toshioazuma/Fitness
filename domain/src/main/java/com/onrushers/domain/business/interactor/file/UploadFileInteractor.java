package com.onrushers.domain.business.interactor.file;

import com.onrushers.domain.business.interactor.Interactor;

import java.io.File;

public interface UploadFileInteractor extends Interactor {

	void setFile(File file, String mimeType);

	void setToken(String token);
}
