
package edu.epcc.epccfallfestapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.epcc.epccfallfestapp.R;
import edu.epcc.epccfallfestapp.model.Game;

/**
 * The GameFragment is the controller for the main view of the application. This fragment
 * links the Game object (which is the model) fragment_main.xml (which is the view). Therefor,
 * the MVC(Model,View,Controller) design pattern is established.
 *
 * File: GameFragment.java
 * Author: Aaron DeWitt
 */
public class GameFragment extends Fragment {

	private static final String TAG = "GameFragment";

	private Game game;
	private Button mScanButton;
	private TextView mCurrentScore;
	private IntentIntegrator scanIntegrator;

	private RelativeLayout ticketBox;
	private EditText ticketBoxText;
	private Button ticketBoxButton;

	private RelativeLayout badConnectionBox;
	private Button badConnectionButton;

	private Activity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mainActivity = activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(mainActivity + " needs to implement View.OnClickListener");
		}
	}

    /**
     * This function is the start of the life for this object. Here at the beginning of this objects
     * lifecycle we are loading a file, if it exists, called game.ser. This file was saved when
     * the games onStop() was called. If this is the first time to run this a new game object is
     * created and the game.ser will be created during the onStop().
     * ********** Important the game state is lost when game is uninstalled ************
     * TODO: find a better system of saving the game - Google API might carry something
     * @param state a Bundle object that carries pertinent information for the creation of this object
     */
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);

        File file = new File(getActivity().getFilesDir(), "game.ser");

        if(file.isFile()){
            try {
			    Log.i(TAG,"Loading saved game...");
			    ObjectInputStream os = new ObjectInputStream(new FileInputStream(file));
			    game = (Game)os.readObject();
			    os.close();
            } catch (Exception e) {
                file.delete();
                game = new Game();
            }
		}else{
			Log.i(TAG,"Loading new game...");
		    game = new Game();
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState){

		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        // The initMonsterImages() function can only be called after a View is created. This
        // function sets all the sprites in place and turns on their visibility;
        game.initMonsterImages(v);

        scanIntegrator = new IntentIntegrator(this);

        mCurrentScore = (TextView)v.findViewById(R.id.current_score);

		ticketBox = (RelativeLayout)v.findViewById(R.id.ticketBoxLayout);
		ticketBoxText = (EditText)v.findViewById(R.id.ticketBoxEditText);
		ticketBoxButton = (Button)v.findViewById(R.id.ticketBoxButton);
		ticketBoxButton.setOnClickListener((View.OnClickListener)mainActivity);

		badConnectionBox = (RelativeLayout)v.findViewById(R.id.badConnectionBox);
		badConnectionButton = (Button)v.findViewById(R.id.badConnectionButton);
        badConnectionButton.setOnClickListener((View.OnClickListener)mainActivity);

        mCurrentScore.setText("" + game.getScore());
		
		mScanButton = (Button)v.findViewById(R.id.photoButton);
		mScanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					if(v.getId()== R.id.photoButton){
						scanIntegrator.initiateScan();
					}
				}catch(Exception e){ e.printStackTrace(); }
			}
		});
		
		return v;
	}

	@Override
	public void onStart(){
		super.onStart();
		if(game.gameEnded()){
			mScanButton.setEnabled(false);
			getFragmentManager().beginTransaction().replace(R.id.game_fragment, new EndGameFragment()).commit();
		}
	}

	@Override
	public void onStop(){
		super.onStop();
		File file = new File(getActivity().getFilesDir(), "game.ser");
		file.delete();
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(game);
			Log.i(TAG,"Writing File "+file.toString());
			os.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
		if(data == null) return;
		
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			
		// Fragments and FragmentManager initialization
		FoundMonsterFragment displayFragment = null;	
		if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Log.i(TAG, "Barcode content: " + scanContent + "\nBarcode format: " + scanFormat + "\n");
            if (scanContent != null) {
                displayFragment = game.update(scanContent);
            }
            mCurrentScore.setText("" + game.getScore());
        }

		Log.i(TAG, "Monsters Found: " + game.monstersFound());
		if(game.monstersFound() == 11){
			mScanButton.setEnabled(false);
			game.setGameEnded(true);
			Log.i(TAG,"End Game Loader");
			getFragmentManager().beginTransaction().replace(R.id.game_fragment, new EndGameFragment()).commit();
		}
			
		if(displayFragment != null)
			getFragmentManager()
					.beginTransaction()
					.add(R.id.game_fragment, displayFragment)
					.addToBackStack(TAG)
					.commit();
		
	}

	public void showTicketBox(boolean show) {
		if (show){
			mScanButton.setEnabled(false);
			ticketBox.setVisibility(RelativeLayout.VISIBLE);
			ticketBoxText.setEnabled(true);
			ticketBoxButton.setEnabled(true);
		} else {
			ticketBoxButton.setEnabled(false);
			ticketBoxText.setEnabled(false);
			ticketBox.setVisibility(RelativeLayout.INVISIBLE);
			mScanButton.setEnabled(true);
		}
	}

	public void showBadConnectionBox() {
        Log.e(TAG,"In showBadConnectionBox()");
		mScanButton.setEnabled(false);
		badConnectionBox.setVisibility(RelativeLayout.VISIBLE);
		badConnectionButton.setEnabled(true);
	}

	public void hideBadConnectionBox() {
		badConnectionButton.setEnabled(false);
		badConnectionBox.setVisibility(RelativeLayout.INVISIBLE);
		mScanButton.setEnabled(true);
	}

	public boolean isRegistered() {
		return game.isRegistered();
	}

	public void register() {
		game.setRegistered(true);
	}

	public String getTicketBoxText() {
		String text = ticketBoxText.getText().toString();
		Log.i(TAG,"ticketBoxText =" + text);
		return text;
	}
}