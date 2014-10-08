package edu.epcc.fallfestapp;


import java.util.UUID;
//this is to check is the push worked
public class Game{
	
	private UUID mId;

	
	private Hint mCurrentHint;
	private Photo mCurrentPhoto;
	private Photo mNewPhoto;
	
	public Game(){
		mId = UUID.randomUUID();
		
		
		
		// The following is for testing the GUI and should be updated
		setCurrentHint(new Hint());
		
	}
	
	public void updatePhoto(Photo newPhoto){
		// a loop through function for testing
		mCurrentPhoto = newPhoto;
		mNewPhoto = mCurrentPhoto;
	}

	public UUID getId() {
		return mId;
	}

	public Hint getCurrentHint() {
		return mCurrentHint;
	}

	public void setCurrentHint(Hint mCurrentHint) {
		this.mCurrentHint = mCurrentHint;
	}

	public Photo getCurrentPhoto() {
		return mCurrentPhoto;
	}

	public void setCurrentPhoto(Photo mCurrentPhoto) {
		this.mCurrentPhoto = mCurrentPhoto;
	}
	
}
