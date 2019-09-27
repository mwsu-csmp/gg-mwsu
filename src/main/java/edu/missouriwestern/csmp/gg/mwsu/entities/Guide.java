package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;
import java.util.logging.Logger;

public class Guide extends Entity implements EventListener {

    private static Logger logger = Logger.getLogger(Guide.class.getCanonicalName());
    private final String[] messages = {"Welcome to GG-Project!",
            "Find the key in order to reach the Spellcraft Area!",
            "Start by searching around for Items!"};

    public Guide(Game game) {
        super(game, Map.of("sprites", "guide",
                           "character", "?",
                            "description", "a friendly guide"));
        game.registerListener(this);
    }

    @Override
    public void acceptEvent(Event event) {
        switch(event.getType()) {
            case "game-start":
                reset(); // move to spawn point at start of game
                break;
            case "command":  // see if someone wants you to talk to them
            if(event.getProperty("command").equals("INTERACT")) {
                logger.info("Interact was pressed");
                var player = getGame().getAgent(event.getProperty("player"));
                if (player instanceof Player) { // TODO: this cast sucks, shouldn't be tied to client, rethink approach
                    var avatar = ((Player) player);
                    var avatarLocation = getGame().getEntityLocation(avatar);
                    if (avatarLocation instanceof Tile) {
                        var tile = (Tile) avatarLocation;
                        var board = tile.getBoard();
                        var target = board.getAdjacentTile(tile, Direction.valueOf(event.getProperty("parameter")));
                        if (target == getGame().getEntityLocation(this)) { // someone is interacting with us
                            getGame().propagateEvent(new Event(getGame(), "speech-event",
                                    Map.of("entity", ""+this.getID(),
                                    "message", messages[(int) (Math.random() * messages.length)])));
                        }
                    }
                }
            }
            break;
        }
    }

    public void reset() {
        var foyer = getGame().getBoard("foyer");
        var location = foyer.getTileStream()  // find location of guide spawn tile (should be exactly 1)
                .filter(tile -> tile.getType().equals("guide-spawn"))
                .findFirst().get();
        getGame().moveEntity(this, location);
    }

    // TODO: convert this to using timeout events
    public void run() {
        while(true) { // walk randomly indefinitely
            try { Thread.sleep(10000); } catch(Exception e) {}
            logger.info("guide walking around");
            var direction = Direction.values()[(int)(Math.random()*4)];
            if(!(getGame().getEntityLocation(this) instanceof  Tile))
                continue; // make sure we're on a tile before we try to move
            var location = (Tile)getGame().getEntityLocation(this);
            var board = location.getBoard();
            var destination = board.getAdjacentTile(location, direction);
            if(destination != null && !destination.hasProperty("impassable")) {
                destination.addEntity(this);
            }
        }
    }

    public String getType() {
        return "npc";
    }

    public String toString() {
        return super.toString();
    }
}
