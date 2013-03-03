package com.moczul.mobilizacja;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.moczul.mobilizacja.R;
import com.moczul.mobilizacja.provider.DataProvider;
import com.moczul.mobilizacja.services.DownloadService;

public class MenuFragment extends SherlockFragment implements OnClickListener {

	public static final String ACTION_DOWNLOAD_COMPLETE = "action_download_complete";
	public static final String ACTION_DOWNLOAD_ERROR = "action_download_error";
	
	private static final String STATE_DIALOG = "progress_dialog_state";
	private static final int DEL_TOKEN = 0;
	
	private Button mGetDataBtn;
	private Button mListBtn;
	private Button	mListAnimBtn;
	private Button	mClearDbBtn;
	private ProgressDialog mProgressDialog;
	
	private class AsyncDBHandler extends AsyncQueryHandler {

		public AsyncDBHandler(ContentResolver cr) {
			super(cr);
		}
		
		@Override
		protected void onDeleteComplete(int token, Object cookie, int result) {
			switch (token) {
			case DEL_TOKEN:
				Toast.makeText(getActivity(), "Removed count: " + result, Toast.LENGTH_SHORT).show();
				return;
			default:
				throw new RuntimeException();
			}
		}
		
	}
	
	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				
				Toast.makeText(getActivity(), "Download completed", Toast.LENGTH_SHORT).show();
			} else if (ACTION_DOWNLOAD_ERROR.equals(action)) {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				
				Toast.makeText(getActivity(), "Error while downloading and parsing data", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	private Receiver mReceiver;
	private AsyncDBHandler mAsyncHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mReceiver = new Receiver(); 
		mAsyncHandler = new AsyncDBHandler(getActivity().getContentResolver());

		View view = inflater.inflate(R.layout.menu_fragment, container, false);
		
		mGetDataBtn = (Button) view.findViewById(R.id.get_data_btn);
		mListBtn = (Button) view.findViewById(R.id.list_view_btn);
		mListAnimBtn = (Button) view.findViewById(R.id.list_view_anim_btn);
		mClearDbBtn = (Button) view.findViewById(R.id.clear_db_btn);
		
		mGetDataBtn.setOnClickListener(this);
		mListBtn.setOnClickListener(this);
		mListAnimBtn.setOnClickListener(this);
		mClearDbBtn.setOnClickListener(this);
		
		if (savedInstanceState != null) {
			boolean showDialog = savedInstanceState.getBoolean(STATE_DIALOG, false);
			if (showDialog)
				showProgressDialog();
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_DOWNLOAD_COMPLETE);
		filter.addAction(ACTION_DOWNLOAD_ERROR);
		getActivity().registerReceiver(mReceiver, filter);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_DIALOG, mProgressDialog != null && mProgressDialog.isShowing());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_data_btn:
			showProgressDialog();
			getActivity().startService(new Intent(DownloadService.ACTION_GET_DATA));
			return;
		case R.id.list_view_btn:
			setListViewFrag(false);
			return;
		case R.id.list_view_anim_btn:
			setListViewFrag(true);
			return;
		case R.id.clear_db_btn:
			mAsyncHandler.startDelete(DEL_TOKEN, null, DataProvider.CONTENT_URI, null, null);
			return;
		default:
			throw new RuntimeException();
		}
	}
	
	private void setListViewFrag(boolean hasAnimation) {
		Bundle extras = new Bundle();
		extras.putBoolean(ListFragment.EXTRAS_HAS_ANIMATION, hasAnimation);
		ListFragment fragment = new ListFragment();
		fragment.setArguments(extras);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_wrapper, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle("Downloading");
		mProgressDialog.setMessage("Downloading and parsing data...");
		mProgressDialog.show();
	}

}
