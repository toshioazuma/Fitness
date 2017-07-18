package com.onrushers.app.feed.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.onrushers.app.R;
import com.onrushers.app.common.fragments.BaseFragment;
import com.onrushers.app.internal.di.components.FeedComponent;
import com.onrushers.domain.business.model.IComment;
import com.onrushers.domain.business.model.IFeed;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedCommentFragment extends BaseFragment
	implements FeedCommentView {

	public static final String TAG = "FeedCommentFr";

	@Bind(R.id.feed_comment_input)
	EditText mCommentEditText;

	@Bind(R.id.feed_comment_publish_button)
	Button mPublishButton;

	@Bind(R.id.feed_comment_recycler)
	RecyclerView mCommentRecyclerView;

	@Inject
	FeedCommentPresenter mPresenter;

	FeedCommentsAdapter mAdapter;


	//region Fragment life cycle
	//----------------------------------------------------------------------------------------------

	public FeedCommentFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getComponent(FeedComponent.class).inject(this);
		mPresenter.setView(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_comment, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mAdapter = new FeedCommentsAdapter();
		mCommentRecyclerView.setAdapter(mAdapter);
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region FeedCommentView
	//----------------------------------------------------------------------------------------------

	@Override
	public void setFeed(IFeed feed) {
		mAdapter.setFeed(feed);
		mPresenter.setFeed(feed);
		mPresenter.loadComments();
	}

	@Override
	public void showComments(List<IComment> comments, int page) {
		mAdapter.appendComments(comments, page);
	}

	@Override
	public void showCreateCommentLoading() {
		mPublishButton.setEnabled(false);
	}

	@Override
	public void hideCreateCommentLoading() {
		mPublishButton.setEnabled(true);
		mCommentEditText.setText("");
	}

	//----------------------------------------------------------------------------------------------
	//endregion

	//region Button click
	//----------------------------------------------------------------------------------------------

	@OnClick(R.id.feed_comment_publish_button)
	public void onPublishClick() {

		String comment = mCommentEditText.getText().toString();
		if (comment.length() > 0) {
			mPresenter.addComment(comment);
		}
	}

	//----------------------------------------------------------------------------------------------
	//endregion
}
