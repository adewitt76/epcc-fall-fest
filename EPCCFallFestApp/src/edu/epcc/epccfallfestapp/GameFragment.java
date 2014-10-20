/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: GameFragment.java
 * Author: Aaron DeWitt & Christian Murga
 */

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
	public static final String BC_ALIEN = "edu.epcc.fall-fest alien";
	public static final String BC_BLOB = "edu.epcc.fall-fest blob";
	public static final String BC_CHUTULU = "edu.epcc.fall-fest chutulu";
	public static final String BC_CLOPS = "edu.epcc.fall-fest clops";
	public static final String BC_FRANKIE = "edu.epcc.fall-fest frankie";
	public static final String BC_MUMMY = "edu.epcc.fall-fest mummy";
	public static final String BC_TOMATO = "edu.epcc.fall-fest tomato";
	public static final String BC_VAMPIRE = "edu.epcc.fall-fest vampire";
	public static final String BC_WEREWOLF = "edu.epcc.fall-fest werewolf";
	public static final String BC_WITCH = "edu.epcc.fall-fest witch";
	public static final String BC_YETI = "edu.epcc.fall-fest yeti";
	
	private ImageView mAlienView;
	private ImageView mBlobView;
	private ImageView mChutuluView;
	private ImageView mClopsView;
	private ImageView mFrankieView;
	private ImageView mMummyView;
	private ImageView mTomatoView;
	private ImageView mVampireView;
	private ImageView mWerewolfView;
	private ImageView mWitchView;
	private ImageView mYetiView;

	// initialization of views pertaining to this fragment
	private final Game mGame = new Game();
	private Button mScanButton;
	IntentIntegrator scanIntegrator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState){
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		View v = inflater.inflate(R.layout.fragment_main, parent, false);
		
		mAlienView = (ImageView)v.findViewById(R.id.image_alien);
		mBlobView = (ImageView)v.findViewById(R.id.image_blob);
		mChutuluView = (ImageView)v.findViewById(R.id.image_chutlu);
		mClopsView = (ImageView)v.findViewById(R.id.image_clops);
		mFrankieView = (ImageView)v.findViewById(R.id.image_frankie);
		mMummyView = (ImageView)v.findViewById(R.id.image_mummy);
		mTomatoView = (ImageView)v.findViewById(R.id.image_tomato);
		mVampireView = (ImageView)v.findViewById(R.id.image_vampire);
		mWerewolfView = (ImageView)v.findViewById(R.id.image_werewolf);
		mWitchView = (ImageView)v.findViewById(R.id.image_witch);
		mYetiView = (ImageView)v.findViewById(R.id.image_yeti);
		
		scanIntegrator = new IntentIntegrator(this);
		
		mScanButton = (Button)v.findViewById(R.id.photoButton);
		mScanButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				try{
					if(v.getId()==R.id.photoButton){
						scanIntegrator.initiateScan();
					}
				}catch(Exception e){}
			}
		});

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
		if(data == null) return;
		
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			
		// Fragments and FragmentManager initialization
		FoundMonsterFragment displayFragment = null;	
		
		String scanContent = scanningResult.getContents();
		String scanFormat = scanningResult.getFormatName();
		Log.i(TAG,"Barcode content: "+scanContent+"\nBarcode format: "+scanFormat+"\n");
		
		if(scanContent.equals(BC_ALIEN)){
			displayFragment = new FoundMonsterFragment(R.layout.alien_fragment);
			mAlienView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_ALIEN);
		}
		
		if(scanContent.equals(BC_BLOB)){
			displayFragment = new FoundMonsterFragment(R.layout.blob_fragment);
			mBlobView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_BLOB);
		}
		
		if(scanContent.equals(BC_CHUTULU)){
			displayFragment = new FoundMonsterFragment(R.layout.chutulu_fragment);
			mChutuluView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_CHUTULU);
		}
		
		if(scanContent.equals(BC_CLOPS)){
			displayFragment = new FoundMonsterFragment(R.layout.clops_fragment);
			mClopsView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_CLOPS);
		}
		
		if(scanContent.equals(BC_FRANKIE)){
			displayFragment = new FoundMonsterFragment(R.layout.frankie_fragment);
			mFrankieView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_FRANKIE);
		}
		
		if(scanContent.equals(BC_MUMMY)){
			displayFragment = new FoundMonsterFragment(R.layout.mummy_fragment);
			mMummyView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_MUMMY);
		}
		
		if(scanContent.equals(BC_TOMATO)){
			displayFragment = new FoundMonsterFragment(R.layout.tomato_fragment);
			mTomatoView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_TOMATO);
		}
		
		if(scanContent.equals(BC_VAMPIRE)){ 
			displayFragment = new FoundMonsterFragment(R.layout.vampire_fragment);
			mVampireView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_VAMPIRE);
			}
		
		if(scanContent.equals(BC_WEREWOLF)){
			displayFragment = new FoundMonsterFragment(R.layout.werewolf_fragment);
			mWerewolfView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_WEREWOLF);
		}
		
		if(scanContent.equals(BC_WITCH)){
			displayFragment = new FoundMonsterFragment(R.layout.witch_fragment);
			mWitchView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_WITCH);
		}
		
		if(scanContent.equals(BC_YETI)){
			displayFragment = new FoundMonsterFragment(R.layout.yeti_fragment);
			mYetiView.setVisibility(ImageView.VISIBLE);
			mGame.update(BC_YETI);
		}
		
		// TODO: Update all display variables
				
		if(displayFragment != null)
			getFragmentManager().beginTransaction().replace(R.id.mainContainer, displayFragment).addToBackStack(TAG).commit();
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
