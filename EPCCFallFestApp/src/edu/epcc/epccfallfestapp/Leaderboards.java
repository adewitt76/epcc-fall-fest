package edu.epcc.epccfallfestapp;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Leaderboards extends Fragment
{
	public static User leaders[]= new User[10];//.... the only problem is the life of the data... I could write to a file, or ask the server for the info
	private static ArrayList<TextView> texts = new ArrayList<TextView>(10);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState)
	{
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View v = inflater.inflate(R.layout.learderboards_fragment, parent, false);
		if(texts.size()==0)
		{
			texts.add((TextView)v.findViewById(R.id.leader0));
			texts.add((TextView)v.findViewById(R.id.leader1));
			texts.add((TextView)v.findViewById(R.id.leader2));
			texts.add((TextView)v.findViewById(R.id.leader3));
			texts.add((TextView)v.findViewById(R.id.leader4));
			texts.add((TextView)v.findViewById(R.id.leader5));
			texts.add((TextView)v.findViewById(R.id.leader6));
			texts.add((TextView)v.findViewById(R.id.leader7));
			texts.add((TextView)v.findViewById(R.id.leader8));
			texts.add((TextView)v.findViewById(R.id.leader9));
		}
		update();
		return v;
	}

	public static boolean update()
	{
		if(texts.size()==0)
			return false;
		for(int i=0; texts.get(i)!=null && i<leaders.length-1; i++)
		{
			if(leaders[i]!=null)
				texts.get(i).setText((i+1)+". "+leaders[i].toString());
			else
				texts.get(i).setText("");
		}
		return true;
	}

	public static class User
	{
		public String name;
		public long score;

		User(String name, long score)
		{
			this.name=name;
			this.score=score;
		}

		public String toString()
		{
			return name+" "+score;
		}
	}

	public static User User(String name, long score)
	{
		return new User(name, score);
	}
//	
//	private void onScan()
//	{
//		long score = Long.parseLong((String)CustomApp.user.get("score"));
//		//modify score
//		CustomApp.user.put("score", score);
//		try {CustomApp.user.save();} //this should update the server
//		catch (ParseException e) {e.printStackTrace();}
//	}
}