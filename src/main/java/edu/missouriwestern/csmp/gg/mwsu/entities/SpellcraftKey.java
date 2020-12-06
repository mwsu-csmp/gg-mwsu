package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;

public class SpellcraftKey extends Entity implements EventListener {

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


    @Override
    public void acceptEvent(Event event)  { //If somebody interacts with a chest
        switch(event.getType()) {
            case "entity-moved":
                var ent = event.getEntity().get();
                var location = getGame().getEntityLocation(ent);
                if(ent instanceof Player && location == getGame().getEntityLocation(this)) {
                    var player = (Player)ent;
                    getGame().moveEntity(this, player);
                }
        }
    }
}
