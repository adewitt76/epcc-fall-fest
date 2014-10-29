package edu.epcc.epccfallfestapp;


import edu.epcc.epccfallfestapp.GameFragment;

//this is to check is the push worked
public class Game{
		
	private String loginName;
	private long score;
	private long startTime;
	private int monstersFound;
	private boolean gameEnded;
	
	private boolean foundAlien;
	private boolean foundBlob;
	private boolean foundChutulu;
	private boolean foundClops;
	private boolean foundFrankie;
	private boolean foundMummy;
	private boolean foundTomato;
	private boolean foundVampire;
	private boolean foundWerewolf;
	private boolean foundWitch;
	private boolean foundYeti;
	
	public Game(){
		score = 0;
		monstersFound = 0;
		startTime = System.currentTimeMillis();
		gameEnded = false;
		
		foundBlob = false;
		foundChutulu = false;
		foundClops = false;
		foundFrankie = false;
		foundMummy = false;
		foundTomato = false;
		foundVampire = false;
		foundWerewolf = false;
		foundWitch = false;
		foundYeti = false;
	}
	
	public boolean update(String BC_ID){
		
		if(BC_ID == GameFragment.BC_ALIEN && !foundAlien){
			// an example of how to use this method
			calcScore(1);
			foundAlien = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_BLOB && !foundBlob){
			// an example of how to use this method
			calcScore(1);
			monstersFound++;
			foundBlob = true;
			return true;
		}else if(BC_ID == GameFragment.BC_CHUTULU && !foundChutulu){
			// an example of how to use this method
			calcScore(1);
			foundChutulu = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_CLOPS && !foundClops){
			// an example of how to use this method
			calcScore(1);
			foundClops = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_FRANKIE && !foundFrankie){
			// an example of how to use this method
			calcScore(1);
			foundFrankie = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_MUMMY && !foundMummy){
			// an example of how to use this method
			calcScore(1);
			foundMummy = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_TOMATO && !foundTomato){
			// an example of how to use this method
			calcScore(1);
			foundTomato = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_VAMPIRE && !foundVampire){
			// an example of how to use this method
			calcScore(1);
			foundVampire = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_WEREWOLF && !foundWerewolf){
			// an example of how to use this method
			calcScore(1);
			foundWerewolf = true;
			monstersFound++;
			return true;
		}else if(BC_ID == GameFragment.BC_WITCH && !foundWitch){
			// an example of how to use this method
			calcScore(1);
			foundWitch = true;
			monstersFound++;
			return true;
		}
		if(BC_ID == GameFragment.BC_YETI && !foundYeti){
			// an example of how to use this method
			calcScore(1);
			foundYeti = true;
			monstersFound++;
			return true;
		}
		else return false;
	}
	
	private void calcScore(int bonusMultiplier)
	{
		long time = System.currentTimeMillis();
		int scoreIndex = ((int)(time-startTime)/1000)*2;
		//you got 83 minutes to find all the monsters to get a score
		score += (10000-scoreIndex)*bonusMultiplier;
	}
	
	public boolean timesUp(){
		long time = System.currentTimeMillis();
		if(((int)(time-startTime)/1000)/60 > 83) return true;
		else return false;
	}
	
	public void setName(String loginName){
		this.loginName = loginName;
	}
	
	public String getName(){
		return loginName;
	}
	
	public void setScore(long score){
		this.score = score;
	}
	
	public long getScore(){
		return score;
	}
	
	public int getMonstersFound() {
		return monstersFound;
	}
	
	public boolean getGameEnded(){
		return gameEnded;
	}
	
	public void setGameEnded(boolean e){
		gameEnded = e;
	}
}