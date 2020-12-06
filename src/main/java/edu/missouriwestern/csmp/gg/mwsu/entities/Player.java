package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;
import java.util.logging.Logger;



/** gives a player a presence within the game */
@Permanent
public class Player extends Entity implements Agent {

    private static Logger logger = Logger.getLogger(Player.class.getCanonicalName());

    private final String id;

    public Player(Game game, String id) {
        super(game, Map.of("player", id,
                "impassable", "true"));
        this.id = id;
    }

    @Override
    public String getAgentID() { return id; }

    @Override
    public String getRole() { return "player"; }

    @Override
    public void acceptEvent(Event event) {
        switch(event.getType()) {
            case "command":
            if(event.getProperty("username").equals(getAgentID())) {
                switch(event.getString("command")) {
                    case "MOVE": // move entity in the specified direction
                        var location = getGame().getEntityLocation(this);
                        if(!(location instanceof Tile)) return;
                        var tile = (Tile)location;
                        var board = tile.getBoard();
                        var direction = event.getDirection("direction").get();
                        final var destination = board.getAdjacentTile(tile, direction);
                        var finalDestination = destination;
                        if(destination != null) { // if we're walking to a valid location...
                            if(destination.hasProperty("impassable") &&
                                    !destination.getProperty("impassable").equals("false"))
                                break;  // can't walk on to an impassable tile

                            if(destination.getEntities()
                                    .filter(ent -> ent.hasProperty("impassable") &&
                                                   !ent.getProperty("impassable").equals("false"))
                                    .findFirst().isPresent()) {
                                // can't walk on to a tile with impassable contents
                                break;
                            }

                            // determine if we walked through a door
                            if(destination.hasProperty("portal-destination-board")) {
                                finalDestination = getGame()  // update destination to new board
                                        .getBoard(destination.getString("portal-destination-board"))
                                        .getTileStream()
                                        .filter(t -> t.column == destination.getInteger("portal-destination-column") &&
                                                     t.row== destination.getInteger("portal-destination-row"))
                                        .findFirst().get();
                            }

                            getGame().moveEntity(this, finalDestination);
                        }
                        break;
                    default:
                        //not sure as what to do
                        //TODO: add logger to this class and put this in log
                        break;

                }
            }
        }
    }

    public void reset() {
        var foyer = getGame().getBoard("outside");
        var location = foyer.getTileStream()  // find location of guide spawn tile (should be exactly 1)
                .filter(tile -> tile.getType().equals("player-spawn"))
                .findFirst().get();
        getGame().moveEntity(this, location);
    }

    public String getType() {
        return getRole();
    }
}
