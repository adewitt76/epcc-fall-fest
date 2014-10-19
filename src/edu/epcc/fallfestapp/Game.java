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
	
	public void update(String BC_ID){
		// TODO: this function is how the Game class will be notified
		if(BC_ID == GameFragment.BC_VAMPIRE){
			// an example of how to use this method
			setCurrentHint(new Hint("You found the Vampire"));
		}
		
		if(BC_ID == GameFragment.BC_YETI){
			// an example of how to use this method
			setCurrentHint(new Hint("You found the Ghost"));
		}
		
		if(BC_ID == GameFragment.BC_MUMMY){
			// an example of how to use this method
			setCurrentHint(new Hint("You found the Mummy"));
		}
	}
}
