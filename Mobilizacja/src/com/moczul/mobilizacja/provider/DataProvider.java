package com.moczul.mobilizacja.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {
	
	private static final int SOUND_ID = 1;
	private static final int SOUNDS = 2;
	
	private static final String AUTHORITY = "com.moczul.mobilizacja.provider";
	private static final String BASE_PATH = "sounds";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/sounds";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/sound";
	
	private static final UriMatcher sUriMatcher = new UriMatcher(0);
	static {
		sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SOUND_ID);
		sUriMatcher.addURI(AUTHORITY, BASE_PATH, SOUNDS);
	}
	
	private DBHelper mDBHelper;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int type = sUriMatcher.match(uri);
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int rows = 0;
		switch (type) {
		case SOUNDS:
			rows = db.delete(SoundTable.SOUND_TABLE, selection, selectionArgs);
			break;
		case SOUND_ID:
			rows = db.delete(SoundTable.SOUND_TABLE, SoundTable._ID + " = ?", new String[] {uri.getLastPathSegment()});
			break;
		default:
			throw new IllegalArgumentException();
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rows;
	}

	@Override
	public String getType(Uri uri) {
		int type = sUriMatcher.match(uri);
		switch (type) {
		case SOUNDS:
			return CONTENT_TYPE;
		case SOUND_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		long id = db.insert(SoundTable.SOUND_TABLE, null, values);
		return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int type = sUriMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(SoundTable.SOUND_TABLE);
		
		switch(type) {
		case SOUNDS:
			break;
		case SOUND_ID:
			queryBuilder.appendWhere(SoundTable._ID + " = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int type = sUriMatcher.match(uri);
		int rows = 0;
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		switch (type) {
		case SOUNDS:
			rows = db.update(SoundTable.SOUND_TABLE, values, selection, selectionArgs);
			break;
		case SOUND_ID:
			rows = db.update(SoundTable.SOUND_TABLE, values, SoundTable._ID + " = ?", new String[] {uri.getLastPathSegment()});
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return rows;
	}

}
