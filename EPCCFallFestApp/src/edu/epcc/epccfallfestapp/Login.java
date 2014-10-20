package edu.epcc.epccfallfestapp;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.login, container);
		final EditText user_name_txt = (EditText)v.findViewById(R.id.login_username), user_password_txt = (EditText)v.findViewById(R.id.login_password);
		((Button)v.findViewById(R.id.login_confirm)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(user_name_txt.getText()!=null && user_password_txt.getText()!=null)
					ParseUser.logInInBackground(user_name_txt.getText().toString(),user_password_txt.getText().toString(), new LogInCallback()
					{
						@Override
						public void done(ParseUser user, ParseException e)
						{
							if (e == null)
								CustomApp.user=user;
							else
								Toast.makeText(getActivity(), "Unable to login", Toast.LENGTH_SHORT).show();
						}
					});
			}
		});
		return v;
	}
}
