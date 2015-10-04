package edu.epcc.epccfallfestapp.model;

import android.view.View;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.controller.FoundMonsterFragment;

/**
 * The main model of the game. A model stores all the data for utilization by the controller.
 * This model stores all the information about the current state of the game for use by our
 * application. The model should be completely decoupled from the view and controller so that
 * any changes to the other components will not have an affect on it. The Game class is Serializable
 * so that the state of the game can be saved.
 *
 * Author: Aaron DeWitt adewitt76@gmail.com
 */
public class Game implements Serializable{
		
	private static final long serialVersionUID = 9203770842176328248L;

	private long score;
	private long startTime;
	private int monstersFound;
	private boolean gameEnded;
	private MonsterSet monsters;

	/**
	 * The constructor initializes only when the game is started for the first time. After the
	 * game has started once it is constructed from a save state. The startTime is documented
	 * here so as to keep track of the time so a score can be assigned. TODO: The startTime variable
	 * should be rethought as the variable is reset when the game is uninstalled and if a client
	 * plays the game more than once this will have to be re-initiated.
	 */
	public Game(){
		score = 0;
		monstersFound = 0;
		startTime = System.currentTimeMillis();
		gameEnded = false;
		monsters = new MonsterSet();
	}

	/**
	 * This method is used to initialize all the images of the monsters on the screen. This was
	 * done as a quick fix because the resource id's are stored in their individual Sprite objects.
	 * This does not make for a loosely coupled object and can create problems when the app is
	 * changed in the future.
	 * TODO: find a way to remove this method so that the Game object is decoupled from the view
	 * @param view an instance of the View where the sprites are displayed.
	 */
	public void initMonsterImages(View view) {
        if (view != null) {
            monsters.initSpriteImages(view);
        }
    }

	/**
	 * This function is called by the controller when a barcode is scanned. It will trickle the
	 * barcode into the Sprite set which will cycle through the sprites to see if any of the
	 * sprites barcodes match the scanned barcode. If there is a match then the sprite will
	 * create a FoundMonsterFragment and return a reference to it. The reference is then passed
	 * back to this function where a score is calculated and the monstersFound is incremented.
	 * The idea of a model holding a reference to a view is yet another poor design choice for
	 * our MVC design pattern and should be rethought.
	 * TODO: find a way to remove the reference of a controller from this model.
	 * @param BC_ID the scanned barcode as a string
	 * @return a reference to a fragment that will display the screen of the found monster
	 */
	public FoundMonsterFragment update(String BC_ID) {
		FoundMonsterFragment foundMonsterFragment = monsters.checkSpritesQRCodes(BC_ID);
		if(foundMonsterFragment != null) {
			calcScore(1);
			monstersFound++;
		}
		return foundMonsterFragment;
	}

	/**
	 * This function calculates the score for a found monster. It is basic and needs to be
     * better thought through.
     * TODO: rework the scoring mechanism of the game.
	 * @param bonusMultiplier the bonus multiplier was originally added to give extra points when
     *                        seen fit, but it is currently not being used.
	 */
	private void calcScore(int bonusMultiplier) {
		long time = System.currentTimeMillis();
		int scoreIndex = ((int)(time-startTime)/1000);
		if(scoreIndex > 9750)  score += 250;
		else score += (10000-scoreIndex)*bonusMultiplier;
	}

    /**
     * Returns the players score.
     * @return the players current score
     */
    public long getScore() {
		return score;
	}

    /**
     * Returns the number of monsters that have been found by the player.
     * @return the number of monsters that have been found by the player
     */
	public int monstersFound() {
		return monstersFound;
	}

    /**
     * Returns whether the game has ended for the player.
     * @return whether or not the game is over
     */
	public boolean gameEnded() {
		return gameEnded;
	}

    /**
     * Sets the game to game over
     * @param e whether or not the game has ended
     */
	public void setGameEnded(boolean e) {
		gameEnded = e;
	}
}