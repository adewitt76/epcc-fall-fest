package edu.epcc.epccfallfestapp;

public class Hint {
	private String hint;
	
	public Hint(){
		// this constructor is strictly for testing purposes
		// THIS CLASS STILL NEEDS TO BE BUILT AND IMPLIMENTED
		
		 hint = "There are skeletons in the governments closet.";
	}

	public Hint(String h){
		hint = h;
	}
	
	public String getHint() {
		return hint;
	}

}
