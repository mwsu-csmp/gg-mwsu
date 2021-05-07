package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;

public class Wizard extends Entity implements EventListener {

    private final String[] messages = {"Get thee to github.com/mwsu-csmp and help build Missouri Wizard State University!"};

    public Wizard(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "wizard",
                "character", "W",
                "impassable", "true",
                "description", "a wise wizard"),
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

    public void reset() {
    }

    public String getType() {
        return "guardian";
    }

    public String toString() {
        return super.toString();
    }
}
