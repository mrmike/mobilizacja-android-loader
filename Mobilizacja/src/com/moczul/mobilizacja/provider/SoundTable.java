package com.moczul.mobilizacja.provider;

public class SoundTable {

	public static final String SOUND_TABLE = "sound";
	public static final String _ID = "_id";
	public static final String GUID = "guid";
	public static final String CREATED_AT = "created_at";
	public static final String STREAMABLE = "streamable";
	public static final String TITLE = "title";
	public static final String DESC = "description";
	public static final String ARTWORK_URL = "artwork_url";
	public static final String PLAYBACK_COUNTS = "playback_counts";
	public static final String FAVORITINGS_COUNTS = "fav_counts";
	
	public static final String CREATE_DB_SQL = "create table "
			+ SOUND_TABLE
			+ "("
			+ _ID + " integer primary key autoincrement, "
			+ GUID + " integer not null, "
			+ CREATED_AT + " text not null, "
			+ STREAMABLE + " integer not null, "
			+ TITLE + " text not null, "
			+ DESC + " text, "
			+ ARTWORK_URL + " text, "
			+ PLAYBACK_COUNTS + " integer, "
			+ FAVORITINGS_COUNTS + " integer"
			+ ");";
	
}
