package com.seapps.takentranslate.Translate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;

import com.seapps.takentranslate.ResultsActivity;

import android.os.AsyncTask;
import android.util.Log;

public class TranslatorAsyncTask extends AsyncTask<String, Void, String> {
	private static final String TAG = null;
	private ResultsActivity callback;

	public TranslatorAsyncTask(ResultsActivity callback) {
		this.callback = callback;
	}

	protected String doInBackground(String... args) {
		String text = args[0];
		try {
			String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=ar&dt=t&q="
					+ URLEncoder.encode(text, "UTF-8");
			Log.d(TAG, "fetching: " + url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			// if (con.getResponseCode() != 200){
			// con.
			// throw new IOException();
			// }
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
			in.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "error downloading file", e);
		}
		return null;
	}

	protected void onPostExecute(String res) {
		if (res != null) {
			String translated = "";
			try {
				JSONArray arr = new JSONArray(res).getJSONArray(0);
				for (int i = 0; i < arr.length(); ++i) {
					translated += arr.getJSONArray(i).getString(0);
				}
				Log.d("JSON", arr.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			callback.translationResult(translated);
		} else {
			callback.translationFailed("Unable to connect to server");
		}
	}
}