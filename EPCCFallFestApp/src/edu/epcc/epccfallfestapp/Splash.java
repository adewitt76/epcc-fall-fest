package edu.epcc.epccfallfestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class Splash extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		final Context context = this;
		new CountDownTimer(1000, 5001)//TODO change time
		{
			@Override
			public void onTick(long millisUntilFinished){}

			@Override
			public void onFinish()
			{
				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);
				finish();
			}
		}.start();
	}
}
