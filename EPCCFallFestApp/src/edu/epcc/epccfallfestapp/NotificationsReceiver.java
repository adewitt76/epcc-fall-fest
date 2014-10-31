package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationsReceiver extends BroadcastReceiver
{
	private static final int mNotificationId = 001;
	private static final String notification = "alert",
			leaderboard = "leaders";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try//try getting json object 
		{
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
			try//try getting alert
			{
				//eg
				//{ "alert": "hello there", "action": "edu.epcc.epccfallfestapp.UPDATE_STATUS" }
				//
				String notificationText = json.getString(notification);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.monster_icon)
				.setContentTitle(notificationText);
				NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService("notification");
				mNotifyMgr.notify(mNotificationId, mBuilder.build());
			}
			catch (JSONException ee)//if notification was not found
			{}
			try//try getting leaderboads
			{
				//
				//{ "leaders": "Aaron;49;Murga;60", "action": "edu.epcc.epccfallfestapp.UPDATE_STATUS" }
				//{ "leaders": "Aaron;49;Murga;60;three;3;four;4;five;5;six;6;seven;7;eigth;8;nine;9;ten;10", "action": "edu.epcc.epccfallfestapp.UPDATE_STATUS" }
				//
				//query.addAscendingOrder("score"); can also just retrieve all users from online, but may be a lot of info
				Scanner input = new Scanner(json.getString(leaderboard));//TODO change format maybe
				input.useDelimiter(";");
				for(int i=0; input.hasNext() && i<Leaderboards.leaders.length; i++)
				{
					String name = input.next().trim();
					if(!input.hasNext())
						break;
					try
					{
						long score = Long.parseLong(input.next().trim());
						Leaderboards.leaders[i] = Leaderboards.User(name,score);
					}
					catch(NumberFormatException e)
					{
						break;
					}
				}
				Leaderboards.update();
				input.close();
			}
			catch (JSONException ee)//if leaderboard was not found
			{}
			try
			{
				String notificationText = json.getString("ticket");
				File f = new File(context.getFilesDir(), "ticket.txt");
				try
				{
					if(!f.exists())
						f.createNewFile();
					PrintWriter out = new PrintWriter(f);
					out.print("gooby plz");
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
			}
			catch (JSONException ee)//if ticket was not found
			{}
		}
		catch (JSONException e){}//if json object was bad
	}
}