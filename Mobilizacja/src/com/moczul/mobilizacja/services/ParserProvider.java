package com.moczul.mobilizacja.services;

import com.moczul.mobilizacja.provider.DataProvider;
import com.moczul.mobilizacja.provider.SoundTable;

import android.content.ContentResolver;
import android.content.ContentValues;

public class ParserProvider {

	private ContentResolver mResolver;

	public ParserProvider(ContentResolver cr) {
		mResolver = cr;
	}

	public void addTrack(long guid, String createdAt, String title,
			String desc, String artwork, boolean streamable,
			int playbackCounts, int favCounts) {

		ContentValues cv = new ContentValues();
		cv.put(SoundTable.GUID, guid);
		cv.put(SoundTable.CREATED_AT, createdAt);
		cv.put(SoundTable.TITLE, title);
		cv.put(SoundTable.DESC, desc);
		cv.put(SoundTable.ARTWORK_URL, artwork);
		cv.put(SoundTable.STREAMABLE, streamable);
		cv.put(SoundTable.PLAYBACK_COUNTS, playbackCounts);
		cv.put(SoundTable.FAVORITINGS_COUNTS, favCounts);
		
		mResolver.insert(DataProvider.CONTENT_URI, cv);
	}
}
