package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class NotificationsReceiver extends BroadcastReceiver
{
	private static final String TAG = "MyNotificationsReceiver",
								notification = "alert",
								leaderboard = "leaders";
	private static final int NUM_OF_LINES=5;
	private static final int mNotificationId = 001;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try//try getting json object 
		{
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			try//try getting alert
			{
				String notificationText = json.getString(notification);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(notificationText);
				NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService("notification");
				mNotifyMgr.notify(mNotificationId, mBuilder.build());
			}
			catch (JSONException ee)//if notification was not found
			{

			}
			
			try//try getting leaderboads
			{
				String notificationText = json.getString(leaderboard);
				//update or create data structure 
			}
			catch (JSONException ee)//if leaderboard was not found
			{

			}
		}
		catch (JSONException e){}//if json object was bad
	}
}



/*

File file = new File(context.getFilesDir(), "latest.txt");
try//try writting to file
{
	Scanner scanner = new Scanner(file);
	ArrayList<String> lines = new ArrayList<String>(NUM_OF_LINES);
	lines.add(notificationText);
	for(int i=0; i<NUM_OF_LINES-1;i++)
	{
		if(scanner.hasNext())
			lines.add(scanner.nextLine());
		else
			break;
	}
	scanner.close();
	PrintWriter write = new PrintWriter(file);
	for(String line : lines)
		write.println(line);
	write.close();
}
catch (FileNotFoundException e)
{
	try
	{
		file.createNewFile();
		PrintWriter write = new PrintWriter(file);
		write.println(notificationText);
		write.close();
	}
	catch (IOException e1) {e1.printStackTrace();}
}
 */