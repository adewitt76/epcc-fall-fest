package edu.epcc.epccfallfestapp.model;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

import edu.epcc.epccfallfestapp.controller.FoundMonsterFragment;

/**
 * Sprites are the data sets for the characters in the game. As Character is a wrapper class
 * for char, Sprite was chosen for an alternate name. The Sprite object holds all the relative
 * data for each character in the game. Here a reference to an ImageView that is a reference to
 * the actual image representation is kept. This is for ease making the sprite visible when it is
 * found.
 *
 * Author: Aaron DeWitt adewitt76@gmail.com
 */
public class Sprite implements Serializable {

    private String name; // can be used to identify a sprite when info is needed
    private boolean found; // whether or not the sprite is found and should be visible
    private transient ImageView image; // a reference to its Image that is on the main_menu view
    private int imageResourceID; // its image resource ID found in res\layout\fragment_main.xml
    private int spriteFragmentID; // its fragment ID found in res\layout\
    private String QRCode; // the barcode representation of the sprite

    /**
     * Builds a new Sprite object.
     * @param name Name used for identifying the object
     * @param imageResourceID the id of its image on the main_menu fragment as given in fragment_main.xml
     * @param spriteFragmentID the id of the sprites fragment that is displayed when found
     * @param QRCode the barcode string that is used to identify the object
     */
    public Sprite(String name, int imageResourceID, int spriteFragmentID, String QRCode) {
        this.name = name;
        found = false;
        image = null;
        this.imageResourceID = imageResourceID;
        this.spriteFragmentID = spriteFragmentID;
        this.QRCode = QRCode;
    }

    /**
     * Checks the scanned barcode against the stored barcode. If there is a match the Sprite is
     * then considered found and made visible a new FoundMonsterFragment is then made and a reference
     * is returned so that it can be displayed. This is probably not the best implementation for
     * this and should be changed.
     * @param qrCode the scanned barcode
     * @return a reference to a new FoundMonsterFragment to be added to the fragment manager for
     * display.
     */
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

    /**
     * This marks the monster as found and sets it's image in the main_menu fragment to visible.
     */
    public void found() {
        found = true;
        image.setVisibility(ImageView.VISIBLE);
    }

    /**
     * Returns whether or not the Sprite has been found.
     * @return
     */
    public boolean isFound() {
        return found;
    }

    /**
     * This method is to set the image view reference. As the reference needs to be set after
     * the view is created. Also it checks if the Sprite has been found and sets it's visibility
     * this is useful when a player is reloading the app.
     * @param view a reference to the view that contains the imageViews of the sprites.
     */
    public void setView(View view) {
        if (view != null) {
            image = (ImageView) view.findViewById(imageResourceID);
            if (found) image.setVisibility(ImageView.VISIBLE);
            else image.setVisibility(ImageView.INVISIBLE);
        }
    }

    public void reset(){
        found = false;
        image.setVisibility(ImageView.INVISIBLE);
    }
}
