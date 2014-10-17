package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CustomApp extends Application
{
	public static ParseUser user;
	public final static String appid = "MwguGls77MeTAseY9zZYW3ZcxuvTDBDm8HgOYlP9",
			clientid = "T8b19F3NOR3JtmJ2ZjdT1BmI3YNBNoOETsPirgo1";

	@Override
	public void onCreate()
	{
		super.onCreate();
		Parse.initialize(this, appid, clientid);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});

		File f = new File(getFilesDir(), "register.txt");
		f.delete();
		if(f.exists())
		{
			try
			{
				Scanner scan = new Scanner(f);
				ParseUser.logInInBackground(scan.nextLine(),scan.nextLine(), new LogInCallback()
				{
					@Override
					public void done(ParseUser user, ParseException e)
					{
						if (e == null)
						{
							CustomApp.user=user;
							Toast.makeText(getApplicationContext(), "You are logged in.", Toast.LENGTH_SHORT).show();

						}
						else
							Toast.makeText(getApplicationContext(), "Unable to login", Toast.LENGTH_SHORT).show();
					}
				});
				scan.close();
			}
			catch (FileNotFoundException e1) {}
		}
	}
}
