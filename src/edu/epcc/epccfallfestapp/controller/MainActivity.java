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
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	// used to establish a connection with Google
	private GoogleApiClient googleApiClient;

	// The following variables are used when a connection fails
	private static int RC_SIGN_IN = 9001;
	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;

	private GameFragment gameFragment;

	public static FragmentManager fm;
	public static FrameLayout frame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		gameFragment = (GameFragment) fm.findFragmentById(R.id.mainContainer);
		if(gameFragment == null){
			gameFragment = new GameFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, gameFragment).commit();
		}

		findViewById(R.id.badConnectionButton).setOnClickListener(this);
	}

	/*
		Here the app tries to sign in silently as a connection with google is required to play the
		game. This is done in the onStart function. If a connection is established the onConnected
		function is called. If a connection cannot be established the on connection failed function
		is called.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		googleApiClient.disconnect();
	}

	// The following are part of the Google APIs
	@Override
	public void onConnected(Bundle bundle) {
		gameFragment.hideBadConnectionBox();
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (mResolvingConnectionFailure) {
			// Already resolving
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

		gameFragment.showBadConnectionBox();
	}

	/*
		onClick listener listens to the button press from the GameFragment to retry sign in.
	 */

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.badConnectionButton) {
			googleApiClient.connect();
		}
	}
}
