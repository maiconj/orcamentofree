package com.orcamentofree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class IntroActivity extends Activity implements Runnable {

	private final int DELAY = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		Toast.makeText(this, "Por favor, aguarde...", DELAY).show();
		Handler h = new Handler();
		h.postDelayed(this, DELAY);
	}

	@Override
	public void run() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

}
