package edu.epcc.epccfallfestapp.Sprite;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.FoundMonsterFragment;

/**
 * Sprites are the data sets for the characters in the game. As Character is a wrapper class
 * for char Sprite was chosen for an alternate name. The Sprite object holds all the relative
 * data for each character in the game.
 *
 * Author: Aaron DeWitt
 * File: Sprite.java
 */
public class Sprite implements Serializable {

    private String name;
    private boolean found;
    private transient ImageView image;
    private int imageResourceID;
    private int spriteFragmentID;
    private String QRCode;

    Sprite(String name, int imageResourceID, int spriteFragmentID, String QRCode) {
        this.name = name;
        found = false;
        image = null;
        this.imageResourceID = imageResourceID;
        this.spriteFragmentID = spriteFragmentID;
        this.QRCode = QRCode;
    }

    public FoundMonsterFragment checkQRCode(String qrCode) {
        if (QRCode.equals(qrCode) && !found) {
            found();
            Bundle bundle = new Bundle();
            bundle.putInt("monster_found", spriteFragmentID);
            FoundMonsterFragment displayFragment = new FoundMonsterFragment();
            displayFragment.setArguments(bundle);
            return displayFragment;
        }
        return null;
    }

    public void found() {
        found = true;
        image.setVisibility(ImageView.VISIBLE);
    }

    public boolean isFound() {
        return found;
    }

    public void setView(View view) {
        if (view != null) {
            image = (ImageView) view.findViewById(imageResourceID);
            if (found) image.setVisibility(ImageView.VISIBLE);
            else image.setVisibility(ImageView.INVISIBLE);
        }
    }
}
