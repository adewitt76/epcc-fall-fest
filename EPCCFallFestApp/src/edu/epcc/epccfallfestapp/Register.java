package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.register, parent, false);
		final EditText user_name_txt = (EditText)v.findViewById(R.id.register_username), user_password_txt = (EditText)v.findViewById(R.id.register_password);
		((Button)v.findViewById(R.id.register_confirm)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(user_name_txt.getText()!=null && user_password_txt.getText()!=null)
				{
					ParseUser user = new ParseUser();
					user.setUsername(user_name_txt.getText().toString());
					user.setPassword(user_password_txt.getText().toString());
					user.put("points", "0");
					//user.setEmail("email@example.com");
					//user.put("phone", "650-555-0000");
					user.signUpInBackground(new SignUpCallback()
					{
						public void done(ParseException e)
						{
							if (e == null)
							{
								// Hooray! Let them use the app now.
								ParseUser.logInInBackground(user_name_txt.getText().toString(),user_password_txt.getText().toString(), new LogInCallback()
								{
									@Override
									public void done(ParseUser user, ParseException e)
									{
										if(e==null)
										{
											CustomApp.user=user;
											File f = new File(getActivity().getFilesDir(), "register.txt");
											try
											{
												if(!f.exists())
													f.createNewFile();
												PrintWriter out = new PrintWriter(f);
												out.print(user_name_txt.getText().toString());
												out.print(user_password_txt.getText().toString());
												out.close();
											}
											catch (FileNotFoundException e1)
											{
												e1.printStackTrace();
											}
											catch (IOException e1)
											{
												e1.printStackTrace();
											}
											Toast.makeText(getActivity(), "You are registered and logged in.", Toast.LENGTH_SHORT).show();
											Fragment newFragment = new GameFragment();
											MainActivity.frame.removeAllViews();
											MainActivity.fm.beginTransaction().add(R.id.fragmentContainer, newFragment).commit();
										}
										else
											Toast.makeText(getActivity(), "Unable to login", Toast.LENGTH_SHORT).show();
									}
								});
							}
							else
							{
								Toast.makeText(getActivity(), "Unable to register.", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
		return v;

	}
}
