package edu.epcc.epccfallfestapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class Splash extends Activity
{
	
	private ImageView mEPCCLogoView;
	private ImageView mCSITLogoView;
	private ImageView mMonsterLogoView;
	
	private Animation animationFadeIn;
	private Animation animationFadeOut;
	private Animation translateAnimation;
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);	
		
		mEPCCLogoView = (ImageView)findViewById(R.id.epcc_logo);
		mCSITLogoView = (ImageView)findViewById(R.id.csit_logo);
		mMonsterLogoView = (ImageView)findViewById(R.id.monster_logo);
		animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		
		mEPCCLogoView.startAnimation(animationFadeIn);
		mEPCCLogoView.setVisibility(ImageView.VISIBLE);
		
		new CountDownTimer(18000, 1000)
		{
			int counter = 1;
			@Override
			public void onTick(long milsLeft){
				if(milsLeft <= 14000 && counter == 1){
					mEPCCLogoView.startAnimation(animationFadeOut);
					mEPCCLogoView.setVisibility(View.GONE);
					counter++;
				}
				if(milsLeft <= 12000 && counter == 2){
					mCSITLogoView.startAnimation(animationFadeIn);
					mCSITLogoView.setVisibility(ImageView.VISIBLE);
					counter++;
				}
				if(milsLeft <= 8000 && counter == 3){
					mCSITLogoView.startAnimation(animationFadeOut);
					mCSITLogoView.setVisibility(View.GONE);
					counter++;
				}
				if(milsLeft <= 6000 && counter == 4){
					mMonsterLogoView.startAnimation(animationFadeIn);
					mMonsterLogoView.setVisibility(ImageView.VISIBLE);
					counter++;
				}
				if(milsLeft <= 2000 && counter == 5){
					mMonsterLogoView.startAnimation(animationFadeOut);
					mMonsterLogoView.setVisibility(ImageView.GONE);
					counter++;
				}
			}

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
