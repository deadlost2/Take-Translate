package com.seapps.takentranslate.Core.TessTool;

import com.seapps.takentranslate.ResultsActivity;
import com.seapps.takentranslate.Core.Imaging.Tools;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class TessAsyncEngine extends AsyncTask<Object, Void, String> {

	static final String TAG = "DBG_" + TessAsyncEngine.class.getName();

	private Bitmap bmp;

	private ResultsActivity callback;

	private int conf;

	@Override
	protected String doInBackground(Object... params) {

		try {

			if (params.length < 2) {
				Log.e(TAG, "Error passing parameter to execute - missing params");
				return null;
			}

			if (!(params[0] instanceof ResultsActivity) || !(params[1] instanceof Bitmap)) {
				Log.e(TAG, "Error passing parameter to execute(context, bitmap)");
				return null;
			}

			callback = (ResultsActivity) params[0];

			bmp = (Bitmap) params[1];

			Log.d(TAG, "filtering bitmap...");

//			bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
			// for (int x = 0; x < bmp.getWidth(); x++) {
			// for (int y = 0; y < bmp.getHeight(); y++) {
			// int grey = bmp.getPixel(x, y) & 0xff;
			// grey = 0xFF000000 | (grey * 0x00010101);
			// bmp.setPixel(x, y, grey);
			// }
			// }
			Log.d(TAG, "filtering bitmap success!");

			if (callback == null || bmp == null) {
				Log.e(TAG, "Error passed null parameter to execute(context, bitmap)");
				return null;
			}

			int rotate = 0;

			if (params.length == 3 && params[2] != null && params[2] instanceof Integer) {
				rotate = (Integer) params[2];
			}

			if (rotate >= -180 && rotate <= 180 && rotate != 0) {
				bmp = Tools.preRotateBitmap(bmp, rotate);
				Log.d(TAG, "Rotated OCR bitmap " + rotate + " degrees");
			}

			Log.d(TAG, "starting tess engine:");
			TessEngine tessEngine = TessEngine.Generate(callback);
			Log.d(TAG, "tess engine started!");

			Log.d(TAG, "starting detect:");
			String result = tessEngine.detectText(bmp);
			conf = tessEngine.getConfidence();
			Log.d(TAG, "detect success!");

			Log.d(TAG, result);

			return result;

		} catch (Exception ex) {
			Log.d(TAG, "Error: " + ex + "\n" + ex.getMessage());
		}

		return null;
	}

	@Override
	protected void onPostExecute(String s) {

		if (s == null || bmp == null || callback == null) {
			callback.ocrFailed();
			return;
		}
		
		callback.ocrSuccess(s,conf);
		// ImageDialog.New().addBitmap(bmp).addTitle(s).show(callback.getFragmentManager(),
		// TAG);

	}
}
