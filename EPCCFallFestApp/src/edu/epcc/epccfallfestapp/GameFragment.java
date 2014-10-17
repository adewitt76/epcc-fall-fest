package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment{

	private static final String TAG = "MonsterFragment";

	// Bar code ID constants.
	public static final String BC_VAMPIRE = "edu.epcc.fall-fest dracula";
	public static final String BC_MUMMY = "edu.epcc.fall-fest Mummy";
	public static final String BC_GHOST = "edu.epcc.fall-fest Ghost";

	private final Game mGame = new Game();
	private ImageView mMonsterImageView;
	private Button mPhotoButton;
	private TextView mHintText;
	IntentIntegrator scanIntegrator;

	@Override
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState){
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		View v = inflater.inflate(R.layout.fragment_game, parent, false);
		
		mMonsterImageView = (ImageView)v.findViewById(R.id.monster_imageView);
		
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
		if(data == null) return;
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			
		// Fragments and FragmentManager initialization
		Fragment displayFragment = null;	
		
		String scanContent = scanningResult.getContents();
		String scanFormat = scanningResult.getFormatName();
		Log.i(TAG,"Barcode content: "+scanContent+"\nBarcode format: "+scanFormat+"\n");
		int resourceID = 0;
		
		if(scanContent.equals(BC_VAMPIRE)){ 
			resourceID = R.drawable.vampire;
			displayFragment = new ExampleFragment();
			mGame.update(BC_VAMPIRE);
			}
		else if(scanContent.equals(BC_MUMMY)){ 
			resourceID = R.drawable.mummy;
			mGame.update(BC_MUMMY);
		}
		else if(scanContent.equals(BC_GHOST)){
			resourceID = R.drawable.ghosts;
			mGame.update(BC_GHOST);
		}
		else resourceID = R.drawable.monster_hunt_title;
		
		// TODO: Update all display variables
		mHintText.setText(mGame.getCurrentHint().getHint());
		
		mMonsterImageView.setImageDrawable(PictureUtils.getScaledDrawable(getActivity(), getResources(), resourceID));
		
		if(displayFragment != null)
			getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, displayFragment).addToBackStack(TAG).commit();
	}
}





/*
Button register = (Button)v.findViewById(R.id.gamefrag_register);
		if(new File(getActivity().getFilesDir(), "register.txt").exists())
			register.setVisibility(View.INVISIBLE);
		else
		{
			register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Fragment newFragment = new Register();
					MainActivity.frame.removeAllViews();
					MainActivity.fm.beginTransaction().add(R.id.fragmentContainer, newFragment).commit();
				}
			});
*/
