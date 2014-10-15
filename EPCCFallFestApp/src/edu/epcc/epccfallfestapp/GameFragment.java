package edu.epcc.epccfallfestapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment{

	private static final String TAG = "MonsterFragment";
	private static final int REQUEST_PHOTO = 0;

	private final Game mGame = new Game();
	private ImageView mPhotoView;
	private Button mPhotoButton;
	private TextView mHintText;

	@Override
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, final Bundle savedInstanceState){
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		View v = inflater.inflate(R.layout.fragment_game, parent, false);
		Button register = (Button)v.findViewById(R.id.gamefrag_register);
		if(new File(getActivity().getFilesDir(), "register.txt").exists())
			register.setVisibility(View.INVISIBLE);
		else
		{
			register.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Fragment newFragment = new Register();
					MainActivity.frame.removeAllViews();
					MainActivity.fm.beginTransaction().add(R.id.fragmentContainer, newFragment).commit();
				}
			});
		}

		mPhotoButton = (Button)v.findViewById(R.id.photoButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), MonsterCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});

		// Check to make sure there is an available camera and disable if not.
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) || 
				(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras()
				> 0);
		if(!hasACamera) mPhotoButton.setEnabled(false);

		mHintText =(TextView)v.findViewById(R.id.hintText);
		mHintText.setText(mGame.getCurrentHint().getHint());

		mPhotoView = (ImageView)v.findViewById(R.id.monster_imageView);

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode != Activity.RESULT_OK) return;

		if(requestCode == REQUEST_PHOTO){
			String filename = data.getStringExtra(MonsterCameraFragment.EXTRA_PHOTO_FILENAME);
			if(filename != null){
				mGame.updatePhoto(new Photo(filename));
				showPhoto();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		showPhoto();
	}

	@Override
	public void onStop(){
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

	private void showPhoto(){
		Photo p = mGame.getCurrentPhoto();
		BitmapDrawable b = null;
		if(p != null){
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}

}
