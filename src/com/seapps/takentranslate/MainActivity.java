package com.seapps.takentranslate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

	static final String TAG = "DBG_" + MainActivity.class.getName();

	private static final int PICK_IMAGE = 0;

	Button galleryButton;
	Button cameraButton;
	Button textButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		galleryButton = (Button) findViewById(R.id.btnGallery);
		galleryButton.setOnClickListener(this);
		cameraButton = (Button) findViewById(R.id.btnCamera);
		cameraButton.setOnClickListener(this);
		textButton = (Button) findViewById(R.id.btnText);
		textButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == galleryButton) {
			chooseImageFromGallery();
		} else if (v == cameraButton) {
			Intent i = new Intent(this, CameraActivity.class);
			startActivity(i);
		} else if (v == textButton) {
			Intent i = new Intent(this, ResultsActivity.class);
			i.putExtra("TEXT", true);
			startActivity(i);
		}
	}

	private void chooseImageFromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("ACTIVITY RESULT", "req = " + requestCode + ", res = " + resultCode + ", data=" + data);
		if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
			recognizeText(data.getData(), null, 0);
		}
	}

	private void recognizeText(Uri uri, Rect box, float angle) {
		Log.d(TAG, "Got bitmap");
		Log.d(TAG, "Initialization of TessBaseApi");

		Intent i = new Intent(this, ResultsActivity.class);
		i.putExtra("EXTRA_IMAGE", uri);
		i.putExtra("EXTRA_BOX", box);
		i.putExtra("EXTRA_ROTATION", angle);

		startActivity(i);
	}

	boolean pressed;

	@Override
	public void onBackPressed() {
		if (pressed)
			super.onBackPressed();
		else {
			pressed = true;
			Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_LONG).show();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					pressed = false;
				}
			};
			galleryButton.postDelayed(r, 3000);
		}
	}
}