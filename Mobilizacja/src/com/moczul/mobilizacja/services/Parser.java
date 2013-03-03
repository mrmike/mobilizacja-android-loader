package com.moczul.mobilizacja.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

	private ParserProvider mProvider;

	public Parser(ParserProvider provider) {
		mProvider = provider;
	}

	public void parse(JSONArray jsonArray) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			if (!obj.has("kind") || !"track".equals(obj.getString("kind")))
				continue;
			
			long guid = obj.getLong("id");
			String createdAt = obj.getString("created_at");
			String title = obj.getString("title");
			String desc = obj.getString("description");
			String artwork = obj.getString("artwork_url");
			boolean streamable = obj.getBoolean("streamable");
			int playbackCounts = getInt(obj, "playback_count", 0);
			int favCounts = getInt(obj, "favoritings_count", 0);

			mProvider.addTrack(guid, createdAt, title, desc, artwork,
					streamable, playbackCounts, favCounts);
		}
	}
	
	private static int getInt(JSONObject obj, String key, int defValue) throws JSONException {
		int result = defValue;
		if (obj.has(key))
			result = obj.getInt(key);
		
		return result;
	}
}
