package edu.missouriwestern.csmp.gg.server.events;

import edu.missouriwestern.csmp.gg.base.Direction;
import edu.missouriwestern.csmp.gg.base.Event;
import edu.missouriwestern.csmp.gg.base.Game;
import edu.missouriwestern.csmp.gg.base.Tile;

import java.util.Map;

public class PushEvent extends Event {

    private final Tile location;
    private final Direction direction;

    public PushEvent(Game game, Tile location, Direction direction) {
        super(game, makeProperties(location, direction));
        this.location = location;
        this.direction = direction;
    }

    public Tile getLocation() { return location; }
    public Direction getDirection() { return direction; }

    public static Map<String,String> makeProperties(Tile location, Direction direction) {
        return Map.of("board", location.getBoard().getName(),
                "column", ""+location.getColumn(),
                "row", ""+location.getRow(),
                "direction", direction.toString());
    }
}
