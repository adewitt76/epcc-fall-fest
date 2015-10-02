/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: GameFragment.java
 * Author: Aaron DeWitt & Christian Murga
 */

package edu.epcc.epccfallfestapp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import edu.epcc.epccfallfestapp.R;


public class Splash extends Activity
{
	private static final String TAG = "SplashActivity";
	
	private ImageView mEPCCLogoView;
	private ImageView mCSITLogoView;
	private ImageView mMonsterLogoView;
	
	private ImageView mSceneOne;
	private ImageView mSceneTwo;
	private ImageView mSceneThree;
	private ImageView mSceneFour;
	
	private Animation animationFadeIn;
	private Animation animationFadeOut;
	
	private MediaPlayer mSongPlayer;
	private MediaPlayer mSirenPlayer;
	
	private static CountDownTimer timer;
	private int timeLeft;
	private int timerCounter;
	private boolean running = true;
	
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mEPCCLogoView = (ImageView)findViewById(R.id.epcc_logo);
		mCSITLogoView = (ImageView)findViewById(R.id.csit_logo);
		mMonsterLogoView = (ImageView)findViewById(R.id.monster_logo);
		
		mSceneOne = (ImageView)findViewById(R.id.starting_scene_one);
		mSceneTwo = (ImageView)findViewById(R.id.starting_scene_two);
		mSceneThree = (ImageView)findViewById(R.id.starting_scene_three);
		mSceneFour = (ImageView)findViewById(R.id.starting_scene_four);
		
		animationFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		animationFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		
		mEPCCLogoView.startAnimation(animationFadeIn);
		mEPCCLogoView.setVisibility(ImageView.VISIBLE);
		
		mSongPlayer = MediaPlayer.create(context, R.raw.app_song);
		mSongPlayer.start();
		
		timeLeft = 42000;
		timerCounter = 1;
		
		RelativeLayout mSplashLayout = (RelativeLayout)findViewById(R.id.splash_screen_rel_layout);
		
		mSplashLayout.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				timer.cancel();
				if(mSongPlayer != null){
					mSongPlayer.release();
					mSongPlayer = null;
				}
				if(mSirenPlayer != null){
					mSirenPlayer.release();
					mSirenPlayer = null;
				}
				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		timer = createTimer();
		timer.start();
	
	}

	@Override
	public void onPause(){
		super.onPause();
		running = false;
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		if(mSongPlayer != null){
			mSongPlayer.pause();
		}
		if(mSirenPlayer != null){
			mSirenPlayer.pause();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		running = true;
		timer = createTimer();
		timer.start();
		if(mSongPlayer != null){
			mSongPlayer.start();
		}
		if(mSirenPlayer != null){
			mSirenPlayer.start();
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		running = false;
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		if(mSongPlayer != null){
			mSongPlayer.release();
			mSongPlayer = null;
		}
		if(mSirenPlayer != null){
			mSirenPlayer.release();
			mSirenPlayer = null;
		}
		finish();
	}
	
	private CountDownTimer createTimer(){
		return new CountDownTimer(timeLeft, 1000)
		{
			
			@Override
			public void onTick(long milsLeft){
				if(running){
				if(milsLeft <= 38000 && timerCounter == 1){
					timeLeft = 38000;
					mEPCCLogoView.startAnimation(animationFadeOut);
					mEPCCLogoView.setVisibility(View.GONE);
					timerCounter++;
				}
				if(milsLeft <= 36000 && timerCounter == 2){
					timeLeft = 36000;
					mCSITLogoView.startAnimation(animationFadeIn);
					mCSITLogoView.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 32000 && timerCounter == 3){
					timeLeft = 32000;
					mCSITLogoView.startAnimation(animationFadeOut);
					mCSITLogoView.setVisibility(View.GONE);
					timerCounter++;
				}
				if(milsLeft <= 30000 && timerCounter == 4){
					timeLeft = 30000;
					mMonsterLogoView.startAnimation(animationFadeIn);
					mMonsterLogoView.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 26000 && timerCounter == 5){
					timeLeft = 26000;
					mMonsterLogoView.startAnimation(animationFadeOut);
					mMonsterLogoView.setVisibility(ImageView.GONE);
					timerCounter++;
				}
				if(milsLeft <= 24000 && timerCounter == 6){
					timeLeft =24000;
					mSceneOne.startAnimation(animationFadeIn);
					mSceneOne.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 20000 && timerCounter == 7){
					timeLeft = 20000;
					mSirenPlayer = MediaPlayer.create(context, R.raw.police_siren);
					mSirenPlayer.setVolume(.25f, 0.0f);
					mSirenPlayer.start();
					mSceneOne.startAnimation(animationFadeOut);
					mSceneOne.setVisibility(View.GONE);
					timerCounter++;
				}
				if(milsLeft <= 18000 && timerCounter == 8){
					timeLeft = 18000;
					mSirenPlayer.setVolume(.5f, 0.25f);
					mSongPlayer.release();
					mSongPlayer = null;
					mSceneTwo.startAnimation(animationFadeIn);
					mSceneTwo.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 14000 && timerCounter == 9){
					timeLeft = 14000;
					mSirenPlayer.setVolume(1.0f, 1.0f);
					mSceneTwo.startAnimation(animationFadeOut);
					mSceneTwo.setVisibility(ImageView.GONE);
					timerCounter++;
				}
				if(milsLeft <= 12000 && timerCounter == 10){
					timeLeft = 12000;
					mSirenPlayer.setVolume(.25f, 0.5f);
					mSceneThree.startAnimation(animationFadeIn);
					mSceneThree.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 8000 && timerCounter == 11){
					timeLeft = 8000;
					mSirenPlayer.setVolume(.0f, 0.25f);
					mSceneThree.startAnimation(animationFadeOut);
					mSceneThree.setVisibility(View.GONE);
					timerCounter++;
				}
				if(milsLeft <= 6000 && timerCounter == 12){
					timeLeft = 6000;
					mSirenPlayer.release();
					mSirenPlayer = null;
					mSongPlayer = MediaPlayer.create(context, R.raw.crickets);
					mSongPlayer.setVolume(.25f, .25f);
					mSongPlayer.start();
					mSceneFour.startAnimation(animationFadeIn);
					mSceneFour.setVisibility(ImageView.VISIBLE);
					timerCounter++;
				}
				if(milsLeft <= 2000 && timerCounter == 13){
					timeLeft = 2000;
					mSceneFour.startAnimation(animationFadeOut);
					mSceneFour.setVisibility(ImageView.GONE);
					timerCounter++;
				}
				}
			}

			@Override
			public void onFinish()
			{
				if(running){
				if(mSongPlayer != null){
					mSongPlayer.release();
					mSongPlayer = null;
				}
				if(mSirenPlayer != null){
					mSirenPlayer.release();
					mSirenPlayer = null;
				}
				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);
				}
				finish();
			}
		};
	}
}
