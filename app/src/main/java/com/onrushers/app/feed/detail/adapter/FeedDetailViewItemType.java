package com.onrushers.app.feed.detail.adapter;

/**
 * Created by Ludovic on 03/09/16.
 */
public enum FeedDetailViewItemType {

	UserHeader(1),
	TagUsers(2),
	TimeAndPlace(3),

	ContentPicture(4),
	ContentText(5),
	ContentRegister(6),

	CountRushAndComment(7),
	Comment(8);


	final int mVal;

	FeedDetailViewItemType(int val) {
		mVal = val;
	}

	public static FeedDetailViewItemType fromVal(int val) {
		if (val == UserHeader.getVal()) {
			return UserHeader;
		} else if (val == TagUsers.getVal()) {
			return TagUsers;
		} else if (val == TimeAndPlace.getVal()) {
			return TimeAndPlace;
		} else if (val == ContentPicture.getVal()) {
			return ContentPicture;
		} else if (val == ContentText.getVal()) {
			return ContentText;
		} else if (val == ContentRegister.getVal()) {
			return ContentRegister;
		} else if (val == CountRushAndComment.getVal()) {
			return CountRushAndComment;
		}

		return Comment;
	}

	public int getVal() {
		return mVal;
	}
}
