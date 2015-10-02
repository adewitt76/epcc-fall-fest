/*
 * EPCC Fall Festival Android app
 * This application is designed as a scavenger hunt game to 
 * be deployed and played at the EPCC Fall Festival.
 * 
 * File: GameFragment.java
 * Author: Aaron DeWitt
 */

package edu.epcc.epccfallfestapp.Model;


import android.view.View;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.FoundMonsterFragment;

public class Game implements Serializable{
		
	private static final long serialVersionUID = 9203770842176328248L;

	private long score;
	private long startTime;
	private int monstersFound;
	private boolean gameEnded;
	private MonsterSet monsters;
	
	public Game(){
		score = 0;
		monstersFound = 0;
		startTime = System.currentTimeMillis();
		gameEnded = false;
		monsters = new MonsterSet();
	}

	public void initMonsterImages(View view) {
        if (view != null) {
            monsters.initSpriteImages(view);
        }
    }
	
	public FoundMonsterFragment update(String BC_ID) {
		FoundMonsterFragment foundMonsterFragment = monsters.checkSpritesQRCodes(BC_ID);
		if(foundMonsterFragment != null) {
			calcScore(1);
			monstersFound++;
		}
		return foundMonsterFragment;
	}
	
	private void calcScore(int bonusMultiplier) {
		long time = System.currentTimeMillis();
		int scoreIndex = ((int)(time-startTime)/1000);
		if(scoreIndex > 9750)  score += 250;
		else score += (10000-scoreIndex)*bonusMultiplier;
	}

    public long getScore() {
		return score;
	}

	public int monstersFound() {
		return monstersFound;
	}

	public boolean gameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean e) {
		gameEnded = e;
	}
}