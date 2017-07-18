package com.onrushers.app.file.impl;

import com.onrushers.app.file.FileClient;
import com.onrushers.domain.business.interactor.file.DownloadFileInteractor;
import com.onrushers.domain.common.DefaultSubscriber;

import java.util.ArrayDeque;
import java.util.Queue;

import javax.inject.Inject;

public class FileClientImpl implements FileClient {

	private static final String TAG = "FileClientImpl";

	private final DownloadFileInteractor mDownloadFileInteractor;
	private       Queue<FileLoadEntry>   mLoadEntriesQueue;

	private FileLoadEntry mProcessingEntry = null;

	@Inject
	public FileClientImpl(DownloadFileInteractor downloadFileInteractor) {
		mDownloadFileInteractor = downloadFileInteractor;
		mLoadEntriesQueue = new ArrayDeque<>();
	}

	@Override
	public void getFile(Integer fileId, FileClient.Receiver fileClientReceiver) {
		if (mLoadEntriesQueue == null) {
			mLoadEntriesQueue = new ArrayDeque<>();
		}
		mLoadEntriesQueue.add(new FileLoadEntry(fileId, fileClientReceiver));
		dequeStack();
	}

	private synchronized void dequeStack() {

		if (mProcessingEntry == null && !mLoadEntriesQueue.isEmpty()) {
			mProcessingEntry = mLoadEntriesQueue.remove();

			if (mProcessingEntry != null) {
				mDownloadFileInteractor.unsubscribe();

				mDownloadFileInteractor.setFileId(mProcessingEntry.fileId);
				mDownloadFileInteractor.execute(new DownloadFileSubscriber(mProcessingEntry.receiver));
			}
		}
	}

	private final class DownloadFileSubscriber extends DefaultSubscriber<String> {

		private final FileClient.Receiver mFileClientReceiver;

		DownloadFileSubscriber(FileClient.Receiver fileClientReceiver) {
			mFileClientReceiver = fileClientReceiver;
		}

		@Override
		public void onError(Throwable e) {
			e.printStackTrace();
		}

		@Override
		public void onNext(String s) {
			mFileClientReceiver.onGetFileUrl(s);

			mProcessingEntry = null;
			dequeStack();
		}
	}

	private class FileLoadEntry {

		private final Integer             fileId;
		private final FileClient.Receiver receiver;

		FileLoadEntry(Integer fileId, FileClient.Receiver receiver) {
			this.fileId = fileId;
			this.receiver = receiver;
		}
	}
}
