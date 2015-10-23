/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: GameFragment.java
 * Author: Aaron DeWitt
 */

package edu.epcc.epccfallfestapp.controller;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import edu.epcc.epccfallfestapp.R;

public class EndGameFragment extends Fragment {
		
	private static final String TAG = "EndGameFragment";
	private MediaPlayer mSongPlayer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.end_game_fragment, parent, false);
		RelativeLayout mMonsterLayout = (RelativeLayout)v.findViewById(R.id.end_game_fragment);
		mMonsterLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		return v;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		mSongPlayer = MediaPlayer.create(getActivity(), R.raw.app_song);
		mSongPlayer.setLooping(true);
		mSongPlayer.start();
	}
	
	public void onPause(){
		super.onPause();
		if(mSongPlayer != null){
			mSongPlayer.stop();
			mSongPlayer.release();
			mSongPlayer = null;
		}
	}
}