package edu.epcc.epccfallfestapp.Model;

import edu.epcc.epccfallfestapp.R;

/**
 * This object is a SpriteSet. All functions of a SpriteSet are kept the SpriteSet object. The idea
 * here is that we have easy access to the necessary data for each of our sprites without the
 * distractions of implementation. Therefor, only a constructor is exists here. And the constructor
 * only contains unique data. This eliminates having to search for this information elsewhere.
 * Author: Aaron DeWitt
 * File: MonsterSet.java
 */
public class MonsterSet extends SpriteSet  {
    public MonsterSet() {
        sprites = new Sprite[11];
        sprites[0] = new Sprite("alien", R.id.image_alien,R.layout.alien_fragment,"edu.epcc.fall-fest alien");
        sprites[1] = new Sprite("blob", R.id.image_blob,R.layout.blob_fragment,"edu.epcc.fall-fest blob");
        sprites[2] = new Sprite("chutulu", R.id.image_chutlu,R.layout.chutulu_fragment,"edu.epcc.fall-fest chutulu");
        sprites[3] = new Sprite("clops", R.id.image_clops,R.layout.clops_fragment,"edu.epcc.fall-fest clops");
        sprites[4] = new Sprite("frankie", R.id.image_frankie,R.layout.frankie_fragment,"edu.epcc.fall-fest frankie");
        sprites[5] = new Sprite("mummy", R.id.image_mummy,R.layout.mummy_fragment,"edu.epcc.fall-fest mummy");
        sprites[6] = new Sprite("tomato", R.id.image_tomato,R.layout.tomato_fragment,"edu.epcc.fall-fest tomato");
        sprites[7] = new Sprite("vampire", R.id.image_vampire,R.layout.vampire_fragment,"edu.epcc.fall-fest vampire");
        sprites[8] = new Sprite("werewolf", R.id.image_werewolf,R.layout.werewolf_fragment,"edu.epcc.fall-fest werewolf");
        sprites[9] = new Sprite("witch", R.id.image_witch,R.layout.witch_fragment,"edu.epcc.fall-fest witch");
        sprites[10] = new Sprite("yeti", R.id.image_yeti,R.layout.yeti_fragment,"edu.epcc.fall-fest yeti");
    }
}
