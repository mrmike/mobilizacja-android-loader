package com.moczul.mobilizacja.adapter;

import com.moczul.mobilizacja.R;
import com.moczul.mobilizacja.provider.SoundTable;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SoundAdapter extends CursorAdapter {

	public static final String[] PROJECTION = new String[] {
		SoundTable._ID, SoundTable.TITLE, SoundTable.DESC,
		SoundTable.FAVORITINGS_COUNTS, SoundTable.PLAYBACK_COUNTS,
		SoundTable.ARTWORK_URL
	};
	
	private static final int TITLE = 1;
	private static final int DESC = 2;
	private static final int FAVS = 3;
	private static final int PLAYBACK = 4;
	private static final int IMG_URL = 5;

	private LayoutInflater mInflater;
	
	private static class ViewHolder {
		TextView title;
		TextView desc;
		TextView playback;
		TextView favs;
		ImageView img;
	}
	
	public SoundAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		String title = cursor.getString(TITLE);
		String desc = cursor.getString(DESC);
		int playbacks = cursor.getInt(PLAYBACK);
		int favs = cursor.getInt(FAVS);
		String imgUrl = cursor.getString(IMG_URL);
		
		viewHolder.title.setText(title);
		viewHolder.desc.setText(desc);
		viewHolder.favs.setText("Favs: " + favs);
		viewHolder.playback.setText("Playbacks: " + playbacks);
		ImageLoader.getInstance().displayImage(imgUrl, viewHolder.img);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup container) {
		View view = mInflater.inflate(R.layout.list_item, container, false);
		ViewHolder viewHolder = new ViewHolder();
		view.setTag(viewHolder);
		viewHolder.title = (TextView) view.findViewById(R.id.list_title);
		viewHolder.desc = (TextView) view.findViewById(R.id.list_desc);
		viewHolder.favs = (TextView) view.findViewById(R.id.list_fav);
		viewHolder.playback = (TextView) view.findViewById(R.id.list_playback);
		viewHolder.img = (ImageView) view.findViewById(R.id.list_img);
		return view;
	}

}
