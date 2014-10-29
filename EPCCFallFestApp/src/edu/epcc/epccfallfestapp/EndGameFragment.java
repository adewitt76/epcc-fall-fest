package edu.epcc.epccfallfestapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EndGameFragment extends Fragment {
		
	private static final String TAG = "EndGameFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		 View v = inflater.inflate(R.layout.end_game_fragment, parent, false);
		return v;
	}
}