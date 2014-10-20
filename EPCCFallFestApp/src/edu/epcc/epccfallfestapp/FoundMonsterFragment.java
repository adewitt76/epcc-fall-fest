package edu.epcc.epccfallfestapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FoundMonsterFragment extends Fragment {
	
	int resource;
	
	FoundMonsterFragment(int resource){
		super();
		this.resource = resource;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		 View v = inflater.inflate(resource, parent, false);
		return v;
	}
}
