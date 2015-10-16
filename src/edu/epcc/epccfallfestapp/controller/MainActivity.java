/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: MainActivity.java
 * Author: Aaron DeWitt
 */

package edu.epcc.epccfallfestapp.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.os.Bundle;

import com.google.android.gms.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;

import edu.epcc.epccfallfestapp.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	private static final String TAG = "MainActivity";

	// used to establish a connection with Google
	private GoogleApiClient googleApiClient;

	// The following variables are used when a connection fails
	private static int RC_SIGN_IN = 9001;
	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;

	public static FragmentManager fm;
	public static FrameLayout frame;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the Google Api Client with access to the Play Games services
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES)
						// add other APIs and scopes here as needed
				.build();


		setContentView(R.layout.activity_fragment);
		frame = (FrameLayout)findViewById(R.id.fragmentContainer);
		fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null){
			fragment = new GameFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		googleApiClient.disconnect();
	}

	// The following are part of the Google APIs
	@Override
	public void onConnected(Bundle bundle) {
		Log.i(TAG, "Connection established");
		GameFragment gameFragment = ((GameFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) gameFragment.hideBadConnectionBox();
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {


		if (mResolvingConnectionFailure) {
			// Already resolving
			mResolvingConnectionFailure = false;
			googleApiClient.connect();
			return;
		}

		// If the sign in button was clicked or if auto sign-in is enabled,
		// launch the sign-in flow
		if (mSignInClicked || mAutoStartSignInFlow) {
			mAutoStartSignInFlow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = true;

			// Attempt to resolve the connection failure using BaseGameUtils.
			// The R.string.signin_other_error value should reference a generic
			// error string in your strings.xml file, such as "There was
			// an issue with sign in, please try again later."
			if (!BaseGameUtils.resolveConnectionFailure(this,
					googleApiClient, connectionResult,
					RC_SIGN_IN, "Cannot connect to Google APIs")) {
				mResolvingConnectionFailure = false;
			}
		}

		GameFragment gameFragment = ((GameFragment) getSupportFragmentManager()
			.findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) gameFragment.showBadConnectionBox();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.badConnectionButton) {
			mSignInClicked = true;
			if(googleApiClient.isConnected()) {
				GameFragment gameFragment = ((GameFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer));
				if(gameFragment != null) gameFragment.hideBadConnectionBox();
			}
			else {
				googleApiClient.connect();
			}
		}
	}
}
