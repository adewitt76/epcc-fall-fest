package edu.epcc.fallfestapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class GameFragment extends Fragment{
	
	private static final String TAG = "MonsterFragment";
	
	private final Game mGame = new Game();
	private TextView mBarcodeTextView;
	private Button mPhotoButton;
	private TextView mHintText;
	IntentIntegrator scanIntegrator;
		
	@Override
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		View v = inflater.inflate(R.layout.fragment_game, parent, false);
		
		mBarcodeTextView = (TextView)v.findViewById(R.id.barcode_textView);
		
		scanIntegrator = new IntentIntegrator(this);
		
		mPhotoButton = (Button)v.findViewById(R.id.photoButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if(v.getId()==R.id.photoButton){
					scanIntegrator.initiateScan();
				}
			}
		});
				
		mHintText =(TextView)v.findViewById(R.id.hintText);
		mHintText.setText(mGame.getCurrentHint().getHint());
		
		return v;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if(scanningResult != null){
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			mBarcodeTextView.setText("Content: "+scanContent+"\nFormat: "+scanFormat+"\n");
			
		} else{
			Toast toast = Toast.makeText(getActivity().getApplicationContext(), "No scan data received", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
}
