package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;
import java.util.logging.Logger;

public class Chest extends Entity implements EventListener, Container {

    private static Logger logger = Logger.getLogger(Guide.class.getCanonicalName());

    public Chest(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "chest-normal",
                "character", "▣",
                "impassable", "true",
                "lid", "closed",
                "description", "a large chest"),
                startingLocation);
    }

    @Override
    public void acceptEvent(Event event)  { //If somebody interacts with a chest
        switch(event.getType()) {
            case "command":
           if (event.getProperty("command").equals("INTERACT")) {
                if (getProperty("lid").equals("closed")) {
                    var player = getGame().getAgent(event.getProperty("username"));
                    if (player instanceof Player) {
                        var avatar = ((Player) player);
                        var avatarLocation = getGame().getEntityLocation(avatar);
                        if (avatarLocation instanceof Tile) {
                            var tile = (Tile) avatarLocation;
                            var board = tile.getBoard();
                            var target = board.getAdjacentTile(tile, Direction.valueOf(event.getProperty("parameter")));
                            if (target == getGame().getEntityLocation(this)) {
                                getEntities().forEach(avatar::addEntity);
                                this.setProperty("lid", "open");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getType() {
        return "chest";
    }
}
