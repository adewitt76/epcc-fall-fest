
/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: MainActivity.java
 * Author: Aaron DeWitt
 */

package edu.epcc.epccfallfestapp;

import java.io.File;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.os.Bundle;

public class MainActivity extends FragmentActivity{

	public static FragmentManager fm;
	public static FrameLayout frame, leader_frame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		frame = (FrameLayout)findViewById(R.id.fragmentContainer);
		leader_frame = (FrameLayout)findViewById(R.id.leader_frame);
		fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null){
			fragment = (new File(getFilesDir(), "register.txt").exists()) ?new GameFragment() : new Register();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
		Fragment drawer = fm.findFragmentById(R.id.leader_frame);
		if(drawer==null)
		{
			drawer = new Leaderboards();
			fm.beginTransaction().add(R.id.leader_frame, drawer).commit();
		}
	}
}