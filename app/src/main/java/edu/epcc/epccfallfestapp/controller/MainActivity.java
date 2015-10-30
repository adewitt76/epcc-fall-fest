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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.example.games.basegameutils.BaseGameUtils;

import edu.epcc.epccfallfestapp.R;
import edu.epcc.epccfallfestapp.backend.registrationApi.RegistrationApi;
import edu.epcc.epccfallfestapp.backend.registrationApi.model.RegistrationBean;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	private static final String TAG = "MainActivity";
    private static final String LEADERS_BOARD_ID = "CgkI4by30bMIEAIQCA";
    private static final int VALID = 6401;
    private static final int USED = 6402;
    private static final int INVALID = 6403;

    private String mRegistrationCode;
    private Menu mMenu;

    private long leaderBoardScore;

	// used to establish a connection with Google
	private GoogleApiClient mGoogleApiClient;

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
		mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        handler = new MessageHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.main_menu, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_leaders_board){
            if (isSignedIn()) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, LEADERS_BOARD_ID,
                        LeaderboardVariant.TIME_SPAN_DAILY, LeaderboardVariant.COLLECTION_PUBLIC),0);
            } else {
                BaseGameUtils.makeSimpleDialog(this, "Leader's board not available").show();
            }
        }
        if(item.getItemId() == R.id.action_sign_out){

        }
        return false;
    }

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		mGoogleApiClient.disconnect();
	}

	// The following are part of the Google APIs
	@Override
	public void onConnected(Bundle bundle) {
		Log.i(TAG, "Connection established");
        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) {
            gameFragment.showBadConnectionBox(false);
            if (!gameFragment.isRegistered()) gameFragment.showTicketBox(true);
        }

        mMenu.findItem(R.id.action_leaders_board).setEnabled(true);
        if(leaderBoardScore < gameFragment.getGame().getScore()) {
            leaderBoardScore = gameFragment.getGame().getScore();
            Games.Leaderboards.submitScore(mGoogleApiClient, LEADERS_BOARD_ID, leaderBoardScore);
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
                    mGoogleApiClient, connectionResult,
					RC_SIGN_IN, "Cannot connect to Google APIs")) {
				mResolvingConnectionFailure = false;
			}
		}

		Log.e(TAG, "Connection Failed");
        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
		if(gameFragment != null) {
			Log.i(TAG,"Found gameFragment");
			gameFragment.showBadConnectionBox(true);
		}
		else Log.e(TAG,"gameFragment == null");
	}

	@Override
	public void onClick(View view) {

        GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));

		if(view.getId() == R.id.badConnectionButton) {
			Log.i(TAG, "badConnectionButton clicked");
			if(mGoogleApiClient.isConnected()) {
				if(gameFragment != null) gameFragment.showBadConnectionBox(false);
			}
			else {
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();
			}
		}

		if(view.getId() == R.id.ticketBoxButton) {
			Log.i(TAG, "ticketBoxButton clicked");
			mRegistrationCode = gameFragment.getTicketBoxText();
            new RegisterAsyncTask().execute();
		}

		if(view.getId() == R.id.invalidTicketOkButton) {
			gameFragment.showInvalidTicketBox(false);
		}
	}

    public void updateLeaderboard(long score) {
        if(isSignedIn()) {
            if(leaderBoardScore < score) {
                leaderBoardScore = score;
                Games.Leaderboards.submitScore(mGoogleApiClient, LEADERS_BOARD_ID, score);
            }
        } else if(mGoogleApiClient == null) Log.e(TAG,"mGoogleApiClient is NULL");
        else Log.e(TAG,"mGoogleApiClient is not connected");
    }

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
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
				regBean = server.getStatus(mRegistrationCode).execute();
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

    /**
     * The following private static inner-class is used to listen to the main message loop. Here it
     * is waiting for a message that is sent from our RegisterAsyncClass. The RegisterAsync object
     * is running on it's own thread and needs to communicate safely back to our activity. That is
     * were this Handler comes in. This class was originally an anonymous inner class, which created
     * a memory link and kept persistent parts of the app on the device.
     */
    private static class MessageHandler extends Handler {

        public MessageHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message message){
			GameFragment gameFragment = ((GameFragment)fm.findFragmentById(R.id.fragmentContainer));
            switch (message.what) {
                case VALID:
                    Log.i(TAG, "Registration number is valid");
                    gameFragment.register();
                    gameFragment.showTicketBox(false);
                    break;
                case USED:
                    Log.e(TAG, "Registration number is used");
					gameFragment.setInvalidTicketBox("used");
					gameFragment.showInvalidTicketBox(true);
                    break;
                case INVALID:
                    Log.e(TAG, "Registration number is invalid");
                    gameFragment.setInvalidTicketBox("invalid");
					gameFragment.showInvalidTicketBox(true);
					break;
                default:
                    super.handleMessage(message);
            }
			gameFragment = null;
        }
    }
}
