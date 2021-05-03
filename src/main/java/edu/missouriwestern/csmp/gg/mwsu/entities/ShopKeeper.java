package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static edu.missouriwestern.csmp.gg.base.Direction.*;

public class ShopKeeper extends Entity implements EventListener, Runnable {

    private static Logger logger = Logger.getLogger(Guide.class.getCanonicalName());

    private int waitStep = 0;

    private final int MOVE_EVERY = 30;


    public ShopKeeper(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "shopkeeper",
                "character", "?",
                "impassable", "true",
                "description", "aggenstein shopkeeper"),
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
                switch(event.getString("command").toLowerCase()) {
                    case "speech":
                        var message = event.getString("message");
                        if(message.startsWith("buy ")) {
                            var item = message.substring(4);
                            switch (item) {
                                case "key":
                                    var myLocation = getGame().getEntityLocation(this);
                                    if(!(myLocation instanceof Tile)) return;  // need to be on the map to interact
                                    var board = ((Tile) myLocation).getBoard();
                                    var dest = board.getAdjacentTile((Tile)myLocation, SOUTH);
                                    dest = board.getAdjacentTile(dest, SOUTH);
                                    dest = board.getAdjacentTile(dest, SOUTH);
                                    var chest = new Chest(getGame(), dest);
                                    var key = new SpellcraftKey(getGame(), chest);
                                    getGame().propagateEvent(new Event(getGame(), "speech",
                                            Map.of("entity", ""+this.getID(),
                                                    "message", "your purchase is in the chest")));
                                    break;
                            }
                        }
                    case "interact":
                        logger.info("Interact was pressed");
                        var player = getGame().getAgent(event.getString("username"));
                        if (player instanceof Player) { // TODO: this cast sucks, shouldn't be tied to client, rethink approach
                            var avatar = ((Player) player);
                            var avatarLocation = getGame().getEntityLocation(avatar);
                            if (avatarLocation instanceof Tile) {
                                var tile = (Tile) avatarLocation;
                                var board = tile.getBoard();
                                if(!event.getDirection().isPresent()) {
                                    break; // interactions must have a direction
                                }
                                var target = board.getAdjacentTile(tile, event.getDirection().get());

                                // get nearby tiles
                                var myLocation = getGame().getEntityLocation(this);
                                if(!(myLocation instanceof Tile)) return;  // need to be on the map to interact
                                var myTile = (Tile)myLocation;
                                var nearbyTiles = new HashSet<Tile>();
                                nearbyTiles.add(myTile);
                                if(board.getAdjacentTile(myTile, Direction.EAST) != null)
                                    nearbyTiles.add(board.getAdjacentTile(myTile, Direction.EAST));
                                if(board.getAdjacentTile(myTile, Direction.WEST) != null)
                                    nearbyTiles.add(board.getAdjacentTile(myTile, Direction.WEST));
                                if(board.getAdjacentTile(myTile, Direction.SOUTH) != null)
                                    nearbyTiles.add(board.getAdjacentTile(myTile, Direction.SOUTH));
                                if(board.getAdjacentTile(myTile, Direction.NORTH) != null)
                                    nearbyTiles.add(board.getAdjacentTile(myTile, Direction.NORTH));
                                if (nearbyTiles.contains(target)) { // someone is interacting with us
                                    getGame().propagateEvent(new Event(getGame(), "speech",
                                            Map.of("entity", ""+this.getID(),
                                                    "message", "what would you like to buy?",
                                                    "responseChoices", List.of(
                                                            "buy potion",
                                                            "buy sword",
                                                            "buy key"))));
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

        logger.info("shopkeeper pacing around");
        var direction = Direction.values()[(int) (Math.random() * 4)];
        if ((getGame().getEntityLocation(this) instanceof Tile)) {
            var location = (Tile) getGame().getEntityLocation(this);
            var board = location.getBoard();
            var destination = board.getAdjacentTile(location, direction);
            if (destination != null && !destination.hasProperty("impassable")) {
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
