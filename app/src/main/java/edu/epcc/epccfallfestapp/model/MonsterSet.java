package edu.epcc.epccfallfestapp.model;

import edu.epcc.epccfallfestapp.R;

/**
 * This object is a SpriteSet. All functions of a SpriteSet are kept the SpriteSet object. The idea
 * here is that we have easy access to the necessary data for each of our sprites without the
 * distractions of implementations. Therefore, only a constructor exists here. And the constructor
 * only contains unique data. This eliminates having to search for this information elsewhere.
 *
 * Author: Aaron DeWitt adewitt76@gmail.com
 */
public class MonsterSet extends SpriteSet  {
    public MonsterSet() {
        sprites = new Sprite[11];
        sprites[0] = new Sprite("alien", R.id.image_alien, R.layout.alien_fragment,"1a4daf52-4472-40ae-a984-804b8aacb1e7");
        sprites[1] = new Sprite("blob", R.id.image_blob, R.layout.blob_fragment,"0dd858b5-7a56-4e61-a8b2-67852ec55b2b");
        sprites[2] = new Sprite("chutulu", R.id.image_chutlu, R.layout.chutulu_fragment,"448fb233-91cf-4f62-8a5c-ef9124bf91c3");
        sprites[3] = new Sprite("clops", R.id.image_clops, R.layout.clops_fragment,"9546f8fc-e4e5-445f-aacf-b4724c2e012c");
        sprites[4] = new Sprite("frankie", R.id.image_frankie, R.layout.frankie_fragment,"e50f8a0a-f768-4a0b-84a1-7f74cdf2d2bc");
        sprites[5] = new Sprite("mummy", R.id.image_mummy, R.layout.mummy_fragment,"67c0b272-8a4b-4c4d-9f7f-0e0d1d6adce4");
        sprites[6] = new Sprite("tomato", R.id.image_tomato, R.layout.tomato_fragment,"74228707-1a2f-447a-9b25-9cf485550aa9");
        sprites[7] = new Sprite("vampire", R.id.image_vampire, R.layout.vampire_fragment,"2c6eb008-fc7d-416d-b5bb-73bfedae96ac");
        sprites[8] = new Sprite("werewolf", R.id.image_werewolf, R.layout.werewolf_fragment,"57985cc1-de02-429a-9b76-2db1fae8d19f");
        sprites[9] = new Sprite("witch", R.id.image_witch, R.layout.witch_fragment,"162ff1cf-9e75-4d90-b8f5-30c054098055");
        sprites[10] = new Sprite("yeti", R.id.image_yeti, R.layout.yeti_fragment,"4ed56641-ca7f-4e6c-a57c-6bba9488cf74");
    }
}
