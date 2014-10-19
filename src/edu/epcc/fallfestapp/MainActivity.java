
/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: MainActivity.java
 * Author: Aaron DeWitt
 */

package edu.epcc.fallfestapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {
	
	FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		mFragmentManager = getSupportFragmentManager();
		Fragment fragment = mFragmentManager.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			fragment = new GameFragment();
			mFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}

}
