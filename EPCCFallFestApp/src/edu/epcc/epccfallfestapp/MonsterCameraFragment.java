package edu.epcc.epccfallfestapp;

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
		
		/*
		 * The following creates a ShutterCallback which implements the ShutterCallback
		 * interface. This is called close to the moment when the camera captures the
		 * image(before it processes the picture). Used here to trigger the progress
		 * indicator but also can be used to do things like play sounds.
		 */
		private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
			@Override
			public void onShutter() {
				// Show progress indicator
				mProgressContainer.setVisibility(View.VISIBLE);
			}
		};
		
		/*
		 * The following creates a PictureCallback which implements the camera's 
		 * PictureCallback interface. This is called after the picture processes the
		 * data into a usable format. Here I am capturing the callback that is
		 * sent when the JPEG image is ready. The picture is sent in a byte array
		 * (byte[] data) and written to a file named newPhoto.jpg and stored in the 
		 * programs sand box(a part of the app that is in accessible to the user).
		 */
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
					
					// Set the flash to be on since the event happens at night
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
					
					// set up the preview and picture sizes
					Log.i(TAG,"Surface - Width: "+width+" Height: "+height);
					Log.i(TAG,"****** Choosing Preview Size ********");
					Size s = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), width, height);
					Log.i(TAG,"Preview Size - Width: "+s.width+" Height: "+s.height);
					parameters.setPreviewSize(s.width, s.height);
					Log.i(TAG,"****** Choosing Picture Size ********");
					s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
					Log.i(TAG,"Picture Size - Width: "+s.width+" Height: "+s.height);
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
		
		
		/**
		 * This method tries to match the best fit size in the list of sizes
		 * with the given width and height.
		 * @param sizes a list of supported sizes
		 * @param width the width that needs to be matched
		 * @param height the height that needs to be matched
		 * @return a Size that is the best fit
		 */
		private Size getBestSupportedSize(List<Size> sizes, int width, int height){
			Size bestSize = sizes.get(0);
			int largestArea = bestSize.width * bestSize.height;
			for(Size s : sizes){
				Log.i(TAG, "Supported sizes - Width: " + s.width + " Height: " + s.height);
			}
			for(Size s : sizes){
				int area = s.width * s.height;
				if(area > largestArea){
					bestSize = s;
					largestArea = area;
				}
			}
			return bestSize;
		}
		
		private Size getOptimalPreviewSize(List<Size> sizes, int width, int height){
			Size bestSize = sizes.get(0);
			int previewArea = width * height;
			for(Size s : sizes){
				Log.i(TAG, "Supported sizes - Width: " + s.width + " Height: " + s.height);
			}
			for(Size s : sizes){
				int area = s.width * s.height;
				if(area > previewArea){
					bestSize = s;
				}
			}
			return bestSize;
		}
}
