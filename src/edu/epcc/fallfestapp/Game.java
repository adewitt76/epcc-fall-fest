package edu.epcc.fallfestapp;


import java.util.UUID;
//this is to check is the push worked
public class Game{
	
	private UUID mId;

	
	private Hint mCurrentHint;

	public Game(){
		mId = UUID.randomUUID();
		
		
		
		// The following is for testing the GUI and should be updated
		setCurrentHint(new Hint());
		
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

}
