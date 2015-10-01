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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	private Game mGame;
	private boolean newGame;
	private Button mScanButton;
	private TextView mCurrentScore;
	IntentIntegrator scanIntegrator;
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		File file = new File(getActivity().getFilesDir(), "game.ser");
		
		try {
			if(file.isFile()){
				Log.i(TAG,"File exists");
				ObjectInputStream os = new ObjectInputStream(new FileInputStream(file));
				mGame = (Game)os.readObject();
				Log.i(TAG,"Score = "+mGame.getScore());
				Log.i(TAG,"Vampire = "+mGame.foundVampire());
				os.close();
				newGame = false;
			}else{
				Log.i(TAG,"File does not exists");
				mGame = new Game();
				newGame = true;
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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

		mCurrentScore = (TextView)v.findViewById(R.id.current_score);
		
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

		if(!newGame) {
			if(mGame.foundAlien()) mAlienView.setVisibility(ImageView.VISIBLE); 
			if(mGame.foundBlob()) mBlobView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundChutulu()) mChutuluView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundClops()) mClopsView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundFrankie()) mFrankieView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundMummy()) mMummyView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundTomato()) mTomatoView.setVisibility(ImageView.VISIBLE); 
			if(mGame.foundVampire()) mVampireView.setVisibility(ImageView.VISIBLE); 
			if(mGame.foundWerewolf()) mWerewolfView.setVisibility(ImageView.VISIBLE); 
			if(mGame.foundWitch()) mWitchView.setVisibility(ImageView.VISIBLE);
			if(mGame.foundYeti()) mYetiView.setVisibility(ImageView.VISIBLE);
			mCurrentScore.setText(""+mGame.getScore());
			
		}
		
		return v;
	}
	
	@Override
	public void onStart(){
		super.onResume();
		if(mGame.gameEnded()){ 
			mScanButton.setEnabled(false);
			getFragmentManager().beginTransaction().replace(R.id.mainContainer, new EndGameFragment()).commit();
		}
	}
	

	@Override
	public void onStop(){
		super.onPause();
		File file = new File(getActivity().getFilesDir(), "game.ser");
		file.delete();
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(mGame);
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
		
		String scanContent = scanningResult.getContents();
		String scanFormat = scanningResult.getFormatName();
		Log.i(TAG,"Barcode content: "+scanContent+"\nBarcode format: "+scanFormat+"\n");
		if(scanContent != null) {
			if (scanContent.equals(BC_ALIEN)) {
				if (mGame.update(BC_ALIEN)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.alien_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mAlienView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_BLOB)) {
				if (mGame.update(BC_BLOB)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.blob_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mBlobView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_CHUTULU)) {
				if (mGame.update(BC_CHUTULU)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.chutulu_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mChutuluView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_CLOPS)) {
				if (mGame.update(BC_CLOPS)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.clops_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mClopsView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_FRANKIE)) {
				if (mGame.update(BC_FRANKIE)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.frankie_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mFrankieView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_MUMMY)) {
				if (mGame.update(BC_MUMMY)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.mummy_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mMummyView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_TOMATO)) {
				if (mGame.update(BC_TOMATO)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.tomato_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mTomatoView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_VAMPIRE)) {
				if (mGame.update(BC_VAMPIRE)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.vampire_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mVampireView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_WEREWOLF)) {
				if (mGame.update(BC_WEREWOLF)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.werewolf_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mWerewolfView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_WITCH)) {
				if (mGame.update(BC_WITCH)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.witch_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mWitchView.setVisibility(ImageView.VISIBLE);
				}
			}

			if (scanContent.equals(BC_YETI)) {
				if (mGame.update(BC_YETI)) {
					Bundle bundle = new Bundle();
					bundle.putInt("monster_found", R.layout.yeti_fragment);
					displayFragment = new FoundMonsterFragment();
					displayFragment.setArguments(bundle);
					mYetiView.setVisibility(ImageView.VISIBLE);
				}
			}
		}
		mCurrentScore.setText(""+mGame.getScore());
		
		Log.i(TAG,"Monsters Found: "+mGame.monstersFound());
		if(mGame.monstersFound() == 11){
			mScanButton.setEnabled(false);
			mGame.setGameEnded(true);
			Log.i(TAG,"End Game Loader");
			getFragmentManager().beginTransaction().replace(R.id.mainContainer, new EndGameFragment()).commit();
		}
			
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
