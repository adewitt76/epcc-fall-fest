
/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: GameFragment.java
 * Author: Aaron DeWitt
 */

package edu.epcc.fallfestapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class GameFragment extends Fragment{
	
	private static final String TAG = "MonsterFragment";
	
	// Bar code ID constants.
	private static final String BC_VAMPIRE = "edu.epcc.fall-fest dracula";
	private static final String BC_MUMMY = "edu.epcc.fall-fest Mummy";
	private static final String BC_GHOST = "edu.epcc.fall-fest Ghost";
	
	// initialization of views pertaining to this fragment
	private final Game mGame = new Game();
	private ImageView mMonsterImageView;
	private Button mPhotoButton;
	private TextView mHintText;
	IntentIntegrator scanIntegrator;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		View v = inflater.inflate(R.layout.fragment_game, parent, false);
		
		mMonsterImageView = (ImageView)v.findViewById(R.id.monster_ImageView);
		
		scanIntegrator = new IntentIntegrator(this);
		
		mPhotoButton = (Button)v.findViewById(R.id.photoButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if(v.getId()==R.id.photoButton){
					scanIntegrator.initiateScan();
				}
			}
		});

		mMonsterImageView.setImageDrawable(PictureUtils.getScaledDrawable(getActivity(), getResources(), R.drawable.monster_hunt_title));
		
		mHintText =(TextView)v.findViewById(R.id.hintText);
		mHintText.setText(mGame.getCurrentHint().getHint());
		
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if(scanningResult == null) return;
			
		// Fragments and FragmentManager initialization
		Fragment displayFragment = null;
		FragmentTransaction transaction;
			
		
		String scanContent = scanningResult.getContents();
		String scanFormat = scanningResult.getFormatName();
		Log.i(TAG,"Barcode content: "+scanContent+"\nBarcode format: "+scanFormat+"\n");
		int resourceID = 0;
		
		if(scanContent.equals(BC_VAMPIRE)){ 
			resourceID = R.drawable.vampire;
			displayFragment = new ExampleFragment();
			
			}
		else if(scanContent.equals(BC_MUMMY)) resourceID = R.drawable.mummy;
		else if(scanContent.equals(BC_GHOST)) resourceID = R.drawable.ghosts;
		else resourceID = R.drawable.monster_hunt_title;
		
		mMonsterImageView.setImageDrawable(PictureUtils.getScaledDrawable(getActivity(), getResources(), resourceID));
		
		if(displayFragment != null){
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainContainer, displayFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}

	}

}



