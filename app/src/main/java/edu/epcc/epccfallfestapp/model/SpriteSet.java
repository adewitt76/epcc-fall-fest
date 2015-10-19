package edu.epcc.epccfallfestapp.model;

import android.view.View;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.controller.FoundMonsterFragment;

/**
 * A sprite set is a collection of sprites that is used to hold all sprites that need to be
 * located during the course of the game. This class holds all methods that work on the Sprites
 * as a whole. The SpriteSet is abstract and the constructor needs to be implemented. The
 * constructor should build all the data for each sprite. The SpriteSet as well as the sprites
 * are Serializable as to be saved with the Game state.
 *
 * Author: Aaron DeWitt adewitt76@gmail.com
 */
public abstract class SpriteSet implements Serializable{

    protected Sprite[] sprites;

    /* ****Implement constructor when inheriting this class**** */

    /**
     * Initiates the Sprites ImageView references as this data needs to collected after the
     * View is created.
     * @param view The view that is holding the sprites Images.
     */
    public void initSpriteImages(View view) {
        for (Sprite sprite: sprites) sprite.setView(view);
    }

    /**
     * The method cycles through each sprite checking the scanned barcode against the Sprites
     * barcode. If a match is found the Sprite is then made visible and a FoundMonsterFragment
     * object is made and the reference is returned.
     * @param qrCode the scanned barcode to check
     * @return a FoundMonsterFragment that needs to be displayed
     */
    public FoundMonsterFragment checkSpritesQRCodes(String qrCode) {
        FoundMonsterFragment rFrag = null;
        for (Sprite sprite: sprites) {
            rFrag = sprite.checkQRCode(qrCode);
            if (rFrag != null) break;
        }
        return rFrag;
    }
}
