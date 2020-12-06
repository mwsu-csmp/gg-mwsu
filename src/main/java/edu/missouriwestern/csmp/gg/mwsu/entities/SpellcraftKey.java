package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.Container;
import edu.missouriwestern.csmp.gg.base.Entity;
import edu.missouriwestern.csmp.gg.base.Game;

import java.util.Map;

public class SpellcraftKey extends Entity {

    public SpellcraftKey(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "key-spellcraft",
                "character", "⚷",
                "impassable", "false",
                "description", "Key to Spellcraft foyer"), startingLocation);
    }

    @Override
    public String getType() {
        return "key";
    }
}
