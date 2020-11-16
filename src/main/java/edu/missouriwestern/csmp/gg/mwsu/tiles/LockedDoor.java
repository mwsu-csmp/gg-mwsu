package edu.missouriwestern.csmp.gg.mwsu.tiles;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.mwsu.entities.Player;

import java.util.Map;
import java.util.logging.Logger;

public class LockedDoor extends Tile implements EventListener {
    private static Logger logger = Logger.getLogger(LockedDoor.class.getCanonicalName());
    private final String[] messages = {"Unlocked the door"};
    private boolean locked = true;
    private Entity key;

    public LockedDoor(int column, int row, Entity key) {
        super(column, row, "locked-door", 'L',
                Map.of( "character", "L",
                        "impassable", "true"));
        this.key = key;
    }

    @Override
    public void acceptEvent(Event event) {
        switch (event.getType()) {
            case "command":
                if (event.getProperty("command").equals("INTERACT")) {
                    logger.info("Interact was pressed");
                    var player = getGame().getAgent(event.getString("username"));
                    if (player instanceof Player) {
                        var avatar = ((Player) player);
                        var avatarLocation = getGame().getEntityLocation(avatar);
                        if (avatarLocation instanceof Tile) {
                            var avatarTile = (Tile) avatarLocation;
                            var board = avatarTile.getBoard();
                            var target = board.getAdjacentTile(avatarTile,
                                    event.getDirection("direction").get());

                            if (target == this) { // someone is interacting with us
                                if (avatar.containsEntity(this.key)) {
                                    locked = false;
                                    this.setProperty("impassable", "false");
                                }
                            }
                        }
                    }
                }
                break;
        }
    }


}


