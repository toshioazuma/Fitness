package com.onrushers.app.file;

public interface FileClient {

	void getFile(Integer fileId, Receiver fileClientReceiver);


	interface Receiver {

		void onGetFileUrl(String fileUrl);
	}
}
