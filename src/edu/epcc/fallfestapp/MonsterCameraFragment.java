package edu.epcc.fallfestapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class MonsterCameraFragment extends Fragment {

		private static final String TAG = "MonsterCameraFragment";
		
		public static final String EXTRA_PHOTO_FILENAME = "edu.epcc.fallfestapp.monsterintent.photo_filename";
		
		private Camera mCamera;
		private SurfaceView mSurfaceView;
		private View mProgressContainer;
		
		private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
			@Override
			public void onShutter() {
				// Show progress indicator
				mProgressContainer.setVisibility(View.VISIBLE);
			}
		};
		private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				String filename = "newPhoto.jpg";
				FileOutputStream os = null;
				boolean success = true;
				
				try{
					os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
					os.write(data);
				}catch(Exception e){
					Log.e(TAG, "Error writing to file " + filename, e);
					success = false;
				}finally{
					try{
						if(os != null) os.close();
					}catch(Exception e){
						Log.e(TAG, "Error writing to file " + filename, e);
						success = false;
					}
				}
				
				if(success){
					Intent i = new Intent();
					i.putExtra(EXTRA_PHOTO_FILENAME, filename);
					getActivity().setResult(Activity.RESULT_OK, i);
				}else{
					getActivity().setResult(Activity.RESULT_CANCELED);
				}
				
				getActivity().finish();
			}
		};
		
		
		@Override
		@SuppressWarnings("deprecation")
		public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
			
			View v = inflater.inflate(R.layout.fragment_monster_camera, parent, false);
			
			mProgressContainer = v.findViewById(R.id.monster_camera_progressContainer);
			mProgressContainer.setVisibility(View.INVISIBLE);
			
			Button takePictureButton = (Button)v.findViewById(R.id.monster_camera_takePictureButton);			
			takePictureButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mCamera != null){
						mCamera.takePicture(mShutterCallback, null, mJpegCallback);
					}
				}
			});
			
			mSurfaceView = (SurfaceView)v.findViewById(R.id.monster_camera_surfaceView);
			SurfaceHolder holder = mSurfaceView.getHolder();
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			holder.addCallback(new SurfaceHolder.Callback() {
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					if(mCamera != null) mCamera.stopPreview();
				}
				
				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					try{
						if(mCamera != null){
							mCamera.setPreviewDisplay(holder);
						}
					} catch (IOException e){
						Log.e(TAG, "Error setting up preview display", e);
					}
				}
				
				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
					if(mCamera == null) return;
					
					// The surface changed size - update preview size
					Camera.Parameters parameters = mCamera.getParameters();
					Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
					parameters.setPreviewSize(s.width, s.height);
					
					// update picture size
					s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
					parameters.setPictureSize(s.width, s.height);
					
					mCamera.setParameters(parameters);
					try{
						mCamera.startPreview();
					}catch(Exception e){
						Log.e(TAG, "Could not start preview", e);
						mCamera.release();
						mCamera = null;
					}
				}
			});
			
			return v;
		}
		
		@TargetApi(9)
		@Override
		public void onResume(){
			super.onResume();
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
				mCamera = Camera.open(0);
			} else {
				mCamera = Camera.open();
			}
		}
		
		@Override
		public void onPause(){
			super.onPause();
			if (mCamera != null){
				mCamera.release();
				mCamera = null;
			}
		}
		
		
		
		private Size getBestSupportedSize(List<Size> sizes, int width, int height){
			Size bestSize = sizes.get(0);
			int largestArea = bestSize.width * bestSize.height;
			for(Size s : sizes){
				int area = s.width * s.height;
				if(area > largestArea){
					bestSize = s;
					largestArea = area;
				}
			}
			return bestSize;
		}
}
