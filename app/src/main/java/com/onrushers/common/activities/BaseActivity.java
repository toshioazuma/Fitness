package com.onrushers.common.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.onrushers.common.utils.KeyboardUtils;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

	@Override
	public void onBackPressed() {
		/**
		 * Hide the keyboard if is shown.
		 */
		KeyboardUtils.hide(this);

		FragmentManager fm = getSupportFragmentManager();

		if (fm.getBackStackEntryCount() > 1) {
			/**
			 * Pop the last fragment.
			 */
			fm.popBackStackImmediate();
		} else {
			/**
			 * Finish this activity.
			 */
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void replaceFragment(int containerId, Fragment fragment, String tag) {

		getSupportFragmentManager()
			.beginTransaction()
			.replace(containerId, fragment, tag)
			.addToBackStack(null)
			.commitAllowingStateLoss();
	}

	public void removeFragment(String tag) {

		getSupportFragmentManager()
			.beginTransaction()
			.remove(getSupportFragmentManager().findFragmentByTag(tag))
			.commitAllowingStateLoss();
	}

	public void clearBackStack() {

	}
}