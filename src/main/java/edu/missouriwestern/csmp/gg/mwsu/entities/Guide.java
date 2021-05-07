package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;
import java.util.logging.Logger;

public class Guide extends Entity implements EventListener, Runnable {

    private final String[] messages = {"Welcome to GG-Project!",
            "Find the key in order to reach the Spellcraft Area!",
            "Start by searching around for Items!"};

    private int waitStep = 0;

    private final int MOVE_EVERY = 10;


    public Guide(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "guide",
                           "character", "?",
                           "impassable", "true",
                            "description", "a friendly guide"),
                startingLocation);
        game.registerListener(this);
    }

    @Override
    public void acceptEvent(Event event) {
        switch(event.getType()) {
            case "game-start":
                reset(); // move to spawn point at start of game
                break;
            case "command":  // see if someone wants you to talk to them
            switch(event.getString("command")) {
                case "INTERACT":
                    var player = getGame().getAgent(event.getString("username"));
                    if (player instanceof Player) { // TODO: this cast sucks, shouldn't be tied to client, rethink approach
                        var avatar = ((Player) player);
                        var avatarLocation = getGame().getEntityLocation(avatar);
                        if (avatarLocation instanceof Tile) {
                            var tile = (Tile) avatarLocation;
                            var board = tile.getBoard();
                            var target = board.getAdjacentTile(tile, event.getDirection("direction").get());
                            if (target == getGame().getEntityLocation(this)) { // someone is interacting with us
                                getGame().propagateEvent(new Event(getGame(), "speech",
                                        Map.of("entity", ""+this.getID(),
                                        "message", messages[(int) (Math.random() * messages.length)])));
                            }
                        }
                    }
                    break;
            }
            break;
        }
    }


    @Override
    public void run() {
        waitStep++;
        if(waitStep % MOVE_EVERY != 0) return;

        var direction = Direction.values()[(int) (Math.random() * 4)];
        if ((getGame().getEntityLocation(this) instanceof Tile)) {
            var location = (Tile) getGame().getEntityLocation(this);
            var board = location.getBoard();
            var destination = board.getAdjacentTile(location, direction);
            if (destination != null &&
                    destination.getRow() > 4 && // keep the guide off the top of the map
                    !destination.hasProperty("impassable")) {
                destination.addEntity(this);
            }
        }
    }

    public void reset() {
    }

    public String getType() {
        return "guardian";
    }

    public String toString() {
        return super.toString();
    }
}
