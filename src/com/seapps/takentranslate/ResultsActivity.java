package com.seapps.takentranslate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.seapps.takentranslate.Core.Imaging.Tools;
import com.seapps.takentranslate.Core.TessTool.TessAsyncEngine;
import com.seapps.takentranslate.Translate.TranslatorAsyncTask;

public class ResultsActivity extends Activity implements OnClickListener {

	private static final String TAG = "TAG_" + ResultsActivity.class.getSimpleName();

	int colors[] = new int[] { Color.RED, Color.rgb(255, 128, 0), Color.YELLOW, Color.CYAN, Color.GREEN, Color.GREEN };

	ImageView img;
	EditText tvSrc;
	TextView tvTrslt, tvConf;
	Button btnTranslate;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		btnTranslate = (Button) findViewById(R.id.btnTranslate);
		img = (ImageView) findViewById(R.id.imgResult);
		tvSrc = (EditText) findViewById(R.id.tvSrcText);
		tvTrslt = (TextView) findViewById(R.id.tvTranslatedText);
		progress = (ProgressBar) findViewById(R.id.progress);
		tvConf = (TextView) findViewById(R.id.tvConfidence);

		btnTranslate.setOnClickListener(this);

		if (getIntent().getBooleanExtra("TEXT", false)) {
			img.setVisibility(View.GONE);
			btnTranslate.setEnabled(true);
			progress.setVisibility(View.GONE);
		} else {
			img.postDelayed(r, 500);
		}
	}

	Runnable r = new Runnable() {

		@Override
		public void run() {
			Uri uri = getIntent().getParcelableExtra("EXTRA_IMAGE");
			Rect box = getIntent().getParcelableExtra("EXTRA_BOX");
			float angle = getIntent().getFloatExtra("EXTRA_ROTATION", 0);
			Log.d(TAG, "Starting Result activity, uri = " + uri + ", box = " + box);
			try {
				Bitmap bmp = null;
				if (box == null) {
					bmp = Media.getBitmap(getContentResolver(), uri);
				} else {
					Bitmap bmp2 = Media.getBitmap(getContentResolver(), uri);
					bmp = Tools.getCroppedBitmap(ResultsActivity.this, bmp2, box, angle);
					bmp2.recycle();
				}
				Log.d(TAG, "Bitmap = " + bmp);
				img.setImageBitmap(bmp);

				new TessAsyncEngine().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ResultsActivity.this, bmp);
			} catch (Exception e) {
				e.printStackTrace();
				ocrFailed();
			}
		}
	};

	public void ocrSuccess(String text, int conf) {
		progress.setVisibility(View.GONE);
		text = text.trim();
		if (text.isEmpty()) {
			tvSrc.setText("[ Nothing detected ]");
		} else {
			btnTranslate.setEnabled(true);
			tvSrc.setText(text);
			tvConf.setText("confidence = " + conf + "%");
			tvConf.setTextColor(colors[conf / 20]);
		}
	}

	@Override
	public void onClick(View v) {
		progress.setVisibility(View.VISIBLE);
		translate(tvSrc.getText().toString());
		btnTranslate.setEnabled(false);
	}

	private void translate(String text) {
		new TranslatorAsyncTask(this).execute(text);
	}

	public void translationResult(String res) {
		progress.setVisibility(View.GONE);
		// btnTranslate.setVisibility(View.GONE);
		btnTranslate.setEnabled(true);
		tvTrslt.setText(res);
	}

	public void translationFailed(String message) {
		Toast.makeText(this, "Cannot translate text, Error: " + message, Toast.LENGTH_LONG).show();
		btnTranslate.setEnabled(true);
		progress.setVisibility(View.GONE);
	}

	public void ocrFailed() {
		Toast.makeText(this, "OCR Failed!", Toast.LENGTH_LONG).show();
		progress.setVisibility(View.GONE);
	}

}
