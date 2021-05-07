package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;
import java.util.logging.Logger;

public class LockedDoor extends Entity implements EventListener {
    private final String[] messages = {"Unlocked the door"};
    private boolean locked = true;
    private String keyType;

    public LockedDoor(Game game, Container startingLocation, String keyType) {
        super(game,
                Map.of( "character", "L",
                        "impassable", "true"),
                startingLocation);
        this.keyType = keyType;
    }

    @Override
    public void acceptEvent(Event event) {
        switch (event.getType()) {
            case "command":
                if (event.getProperty("command").equals("INTERACT")) {
                    var player = getGame().getAgent(event.getString("username"));
                    if (player instanceof Player) {
                        var avatar = ((Player) player);
                        var avatarLocation = getGame().getEntityLocation(avatar);
                        if (avatarLocation instanceof Tile) {
                            var avatarTile = (Tile) avatarLocation;
                            var board = avatarTile.getBoard();
                            var target = board.getAdjacentTile(avatarTile,
                                    event.getDirection("direction").get());

                            if (target == getGame().getEntityLocation(this)) { // someone is interacting with us
                                if (avatar.getEntities().filter(ent -> ent.getType().equals(keyType)).findFirst().isPresent()) {
                                    locked = false;
                                    this.setProperty("impassable", "false");
                                    getGame().removeEntity(this);
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    public String getType() {
        return "caveentry";
    }

}


