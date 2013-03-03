package com.moczul.mobilizacja;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.moczul.mobilizacja.R;
import com.moczul.mobilizacja.adapter.SoundAdapter;
import com.moczul.mobilizacja.provider.DataProvider;

public class ListFragment extends SherlockFragment implements LoaderCallbacks<Cursor> {
	
	public static final String EXTRAS_HAS_ANIMATION = "list_has_animation";

	private static final int LOADER_SOUNDS_ID = 0;
	
	private ListView mListView;
	private ProgressBar mProgress;
	private TextView mNoItemInfo;
	
	private SoundAdapter mAdapter;
	private boolean mHasAnimation;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle extras = getArguments();
		if (extras == null)
			throw new RuntimeException();

		mHasAnimation = extras.getBoolean(EXTRAS_HAS_ANIMATION, false);
		int layout = mHasAnimation ? R.layout.anim_list_fragment : R.layout.list_fragment;

		View view = inflater.inflate(layout, container, false);
		
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mListView = (ListView) view.findViewById(R.id.list_view);
		mProgress = (ProgressBar) view.findViewById(R.id.progress_bar);
		mNoItemInfo = (TextView) view.findViewById(R.id.no_items_info);
		
		mAdapter = new SoundAdapter(getActivity(), null, 0);
		mListView.setAdapter(mAdapter);
		
		getLoaderManager().initLoader(LOADER_SOUNDS_ID, null, this);
		
		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getFragmentManager().popBackStack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle extras) {
		mProgress.setVisibility(mHasAnimation ? View.GONE : View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mNoItemInfo.setVisibility(View.GONE);
		return new CursorLoader(getActivity(), DataProvider.CONTENT_URI, SoundAdapter.PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		mProgress.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		mNoItemInfo.setVisibility(cursor.getCount() == 0 ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		mAdapter.swapCursor(null);
	}
	
}
