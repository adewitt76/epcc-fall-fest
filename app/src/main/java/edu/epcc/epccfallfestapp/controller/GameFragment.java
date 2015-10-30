
package edu.epcc.epccfallfestapp.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.epcc.epccfallfestapp.R;
import edu.epcc.epccfallfestapp.model.Game;

/**
 * The GameFragment is the controller for the main_menu view of the application. This fragment
 * links the Game object (which is the model) fragment_main.xml (which is the view). Therefor,
 * the MVC(Model,View,Controller) design pattern is established.
 *
 * File: GameFragment.java
 * Author: Aaron DeWitt
 */
public class GameFragment extends Fragment {

	private static final String TAG = "GameFragment";

	private long mScore;
	private boolean backFromMonsterFragment;

	private Game game;
	private Button mScanButton;
	private TextView mCurrentScore;
	private IntentIntegrator scanIntegrator;

	private RelativeLayout ticketBox;
	private EditText ticketBoxText;
	private Button ticketBoxButton;

	private RelativeLayout badConnectionBox;
	private Button badConnectionButton;

	private RelativeLayout invalidTicketBox;
	private TextView invalidTicketTextView;
	private TextView invalidTicketDescTextView;
	private Button invalidTicketButton;

    private RelativeLayout gameCompleteDialog;
    private TextView gameCompleteScore;

	private MainActivity mCallback;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (MainActivity)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(mCallback + " needs to implement View.OnClickListener");
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

		mScore = game.getScore();
		backFromMonsterFragment = false;

		getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (game.monstersFound() == 11) {
                    if (backFromMonsterFragment) {
                        backFromMonsterFragment = false;
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.game_fragment, new EndGameFragment())
                                .addToBackStack(TAG)
                                .commit();
                        game.setGameCompleted(true);
                        showGameCompleteDialog(true);
                    }
                    Log.i(TAG, "Monsters Found: " + game.monstersFound());
                    if (game.gameEnded() == false) {
                        mScanButton.setEnabled(false);
                        game.setGameEnded(true);
                        backFromMonsterFragment = true;
                    }
                }
            }
        });
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
		ticketBoxButton.setOnClickListener(mCallback);

		badConnectionBox = (RelativeLayout)v.findViewById(R.id.badConnectionBox);
		badConnectionButton = (Button)v.findViewById(R.id.badConnectionButton);
        badConnectionButton.setOnClickListener(mCallback);

		invalidTicketBox = (RelativeLayout)v.findViewById(R.id.invalidTicket);
		invalidTicketTextView = (TextView)v.findViewById(R.id.invalidTicketTextView);
		invalidTicketDescTextView = (TextView)v.findViewById(R.id.invalidTicketDescTextView);
		invalidTicketButton = (Button)v.findViewById(R.id.invalidTicketOkButton);
		invalidTicketButton.setOnClickListener(mCallback);

        gameCompleteDialog = (RelativeLayout)v.findViewById(R.id.gameCompleteDialog);
        gameCompleteScore = (TextView)v.findViewById(R.id.gameCompleteDialogScoreTextView);

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

        if(game.isGameCompleted()) showGameCompleteDialog(true);
        else showGameCompleteDialog(false);

		return v;
	}

	@Override
	public void onStart(){
		super.onStart();
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

		if(displayFragment != null) {
			getFragmentManager()
					.beginTransaction()
					.add(R.id.game_fragment, displayFragment)
					.addToBackStack(TAG)
					.commit();
		}
		if(mScore != game.getScore()) {
			mScore = game.getScore();
			mCallback.updateLeaderboard(mScore);
		}


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

	public void showBadConnectionBox(boolean show) {
		if (show) {
			mScanButton.setEnabled(false);
			badConnectionBox.setVisibility(RelativeLayout.VISIBLE);
			badConnectionButton.setEnabled(true);
		} else {
			badConnectionButton.setEnabled(false);
			badConnectionBox.setVisibility(RelativeLayout.INVISIBLE);
			mScanButton.setEnabled(true);
		}
	}

	public void setInvalidTicketBox(String setting) {
		if (setting.equals("invalid")) {
			invalidTicketTextView.setText(R.string.invalid_ticket);
			invalidTicketDescTextView.setText(R.string.invalid_ticket_description);
		} else if (setting.equals("used")) {
			invalidTicketTextView.setText(R.string.used_ticket);
			invalidTicketDescTextView.setText(R.string.used_ticket_description);
		}
	}

	public void showInvalidTicketBox(boolean show) {
		if (show) {
			invalidTicketBox.setVisibility(RelativeLayout.VISIBLE);
			invalidTicketButton.setEnabled(true);
		} else {
			invalidTicketButton.setEnabled(false);
			invalidTicketBox.setVisibility(RelativeLayout.INVISIBLE);
		}
	}

    public void showGameCompleteDialog(boolean show)  {
        if (show) {
            mScanButton.setEnabled(false);
            gameCompleteScore.setText("Your score: " + game.getScore());
            gameCompleteDialog.setVisibility(RelativeLayout.VISIBLE);
        } else {
            gameCompleteDialog.setVisibility(RelativeLayout.INVISIBLE);
            mScanButton.setEnabled(true);
        }
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

	public Game getGame() {
		return game;
	}
}
