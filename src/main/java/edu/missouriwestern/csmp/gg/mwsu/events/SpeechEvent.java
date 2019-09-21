package edu.missouriwestern.csmp.gg.mwsu.events;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;

public class SpeechEvent extends Event {

    private final Entity entity;
    private final String message;
    private final Board board;

    public SpeechEvent(Game game, Entity entity, String message) {
        super(game, getParameters(entity, message));
        this.entity = entity;
        this.message = message;
        var location = game.getEntityLocation(entity);
        if(location instanceof Tile)
            board = ((Tile)location).getBoard();
        else board = null;
    }

    public Entity getEntity() { return entity; }
    public String getMessage() { return message; }
    public Board getBoard() { return board; }

    public static Map<String,String> getParameters(Entity entity, String message) {
        return Map.of("entity", entity.getID()+"",
                "message", message);
    }
}
