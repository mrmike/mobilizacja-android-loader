package com.moczul.mobilizacja.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;

import com.moczul.mobilizacja.MenuFragment;

public class DownloadService extends IntentService {

	public static final String ACTION_GET_DATA = "com.moczul.mobilizacja.GET_DATA";

	private static final String USER_ID = "SOUNDCLOUD_USER_ID"; 
	private static final String CONSUMER_KEY = "SOUNDCLOUD_CONSUMER_KEY";
	
	private static final String API_URL = String.format("https://api.soundcloud.com/users/%s/favorites.json?consumer_key=%s", USER_ID, CONSUMER_KEY);

	private Parser mParser;
	
	public DownloadService() {
		this("DownloadService");
	}

	public DownloadService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ParserProvider parserProvider = new ParserProvider(getContentResolver());
		mParser = new Parser(parserProvider);
		String action = intent.getAction();
		if (ACTION_GET_DATA.equals(action)) {
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(API_URL);
			
			try {
				HttpResponse response = client.execute(getRequest);
				if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
					// handle error, return
				}
				
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				JSONArray array = new JSONArray(builder.toString());
				mParser.parse(array);
				sendBroadcast(new Intent(MenuFragment.ACTION_DOWNLOAD_COMPLETE));
			} catch (ClientProtocolException e) {
				handleError(e);
			} catch (IOException e) {
				handleError(e);
			} catch (JSONException e) {
				handleError(e);
			}
		}
	}
	
	private void handleError(Exception e) {
		e.printStackTrace();
		sendBroadcast(new Intent(MenuFragment.ACTION_DOWNLOAD_ERROR));
	}

}
