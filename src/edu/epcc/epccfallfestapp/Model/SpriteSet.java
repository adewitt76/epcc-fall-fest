package edu.epcc.epccfallfestapp.model;

import android.view.View;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.controller.FoundMonsterFragment;

/**
 * A sprite set is a collection of sprites that is used to hold all sprites that need to be
 * located during the course of the game. This class holds all methods that work on the Sprites
 * as a whole.
 *
 * Author: Aaron DeWitt
 * File: SpriteSet.java
 */
public abstract class SpriteSet implements Serializable{

    protected Sprite[] sprites;

    /* TODO: Implement constructor when inheriting this class */

    public void initSpriteImages(View view) {
        for (Sprite sprite: sprites) sprite.setView(view);
    }

    public FoundMonsterFragment checkSpritesQRCodes(String qrCode) {
        FoundMonsterFragment rFrag = null;
        for (Sprite sprite: sprites) {
            rFrag = sprite.checkQRCode(qrCode);
            if (rFrag != null) break;
        }
        return rFrag;
    }
}
