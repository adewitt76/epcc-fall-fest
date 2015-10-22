/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: MainActivity.java
 * Author: Aaron DeWitt
 */

package edu.epcc.epccfallfestapp.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.example.games.basegameutils.BaseGameUtils;

import edu.epcc.epccfallfestapp.R;
import edu.epcc.epccfallfestapp.backend.registrationApi.RegistrationApi;
import edu.epcc.epccfallfestapp.backend.registrationApi.model.RegistrationBean;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	private static final String TAG = "MainActivity";
    private static final int VALID = 6401;
    private static final int USED = 6402;
    private static final int INVALID = 6403;

    private String registrationCode;

	// used to establish a connection with Google
	private GoogleApiClient googleApiClient;

	// The following variables are used when a connection fails
	private static int RC_SIGN_IN = 9001;
	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;

	public static FragmentManager fm;
	public static FrameLayout frame;

    // handler handles thread messages
    public static Handler handler;

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

        // create a handler for thread communications
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message){
                switch (message.what) {
                    case VALID:
						Log.i(TAG, "Registration number is valid");
                        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
                        gameFragment.register();
                        gameFragment.showTicketBox(false);
                        break;
                    case USED:
						Log.e(TAG, "Registration number is used");
                        break;
                    case INVALID:
						Log.e(TAG, "Registration number is invalid");
                        break;
                    default:
                        super.handleMessage(message);
                }
            }
        };
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
        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) {
            gameFragment.hideBadConnectionBox();
            if (!gameFragment.isRegistered()) gameFragment.showTicketBox(true);
        }
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		if(connectionResult.getErrorCode() == ConnectionResult.SIGN_IN_REQUIRED){
			Log.e(TAG,"Sign in required");
		}

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

		Log.e(TAG,"Connection Failed");
        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) {
			Log.i(TAG,"Found gameFragment");
			gameFragment.showBadConnectionBox();
		}
		else Log.e(TAG,"gameFragment == null");
	}

	@Override
	public void onClick(View view) {

        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));

		if(view.getId() == R.id.badConnectionButton) {
			Log.i(TAG, "badConnectionButton clicked");
			if(googleApiClient.isConnected()) {
				if(gameFragment != null) gameFragment.hideBadConnectionBox();
			}
			else {
				googleApiClient.disconnect();
				googleApiClient.connect();
			}
		}
		if(view.getId() == R.id.ticketBoxButton) {
			Log.i(TAG, "ticketBoxButton clicked");
			registrationCode = gameFragment.getTicketBoxText();
            new RegisterAsyncTask().execute();

		}
	}

    private void sendRegistrationResults(int status, RegistrationBean bean) {
		Message message;
		message = handler.obtainMessage(status, bean);
		message.sendToTarget();
    }

	private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
		protected Void doInBackground(Void... params) {
			RegistrationBean regBean = null;
			try{
				RegistrationApi.Builder builder = new RegistrationApi.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
			    RegistrationApi server = builder.build();
				regBean = server.getStatus(registrationCode).execute();
            } catch (Exception e) {
                Log.e(TAG,"Could not retrieve registration status: " + e.getClass().toString());
			}
			if(regBean != null) {
				Log.i(TAG,"Registration status:"+regBean.getRegistrationCodeStatus());
				sendRegistrationResults(regBean.getRegistrationCodeStatus(),regBean);
			}
			return null;
		}
	}
}
