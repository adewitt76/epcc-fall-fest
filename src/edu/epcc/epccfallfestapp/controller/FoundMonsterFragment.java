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

public class FoundMonsterFragment extends Fragment {

	MediaPlayer mPlayer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        int monster_resource = getArguments().getInt("monster_found");

		View v = inflater.inflate(monster_resource, parent, false);

		RelativeLayout mMonsterLayout = (RelativeLayout)v.findViewById(R.id.found_monster_layout);
		
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
		mPlayer = MediaPlayer.create(getActivity(), R.raw.ending);
		mPlayer.start();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(mPlayer != null){
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
		
	}
}
