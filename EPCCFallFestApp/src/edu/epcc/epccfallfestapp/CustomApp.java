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
import com.parse.ParseQuery;
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
		login();
	}

	public void login()
	{
		File f = new File(getFilesDir(), "register.txt");
		try
		{
			Scanner info = new Scanner(f);
			ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
			user = query.get(info.nextLine());
			info.close();
		}
		catch (FileNotFoundException e) {}
		catch (ParseException e) {}
	}

}
