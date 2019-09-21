package edu.missouriwestern.csmp.gg.mwsu.events;

import edu.missouriwestern.csmp.gg.base.*;

import java.util.Map;

public class FlagEvent extends Event {


    private final String message;


    public FlagEvent(Game game, String message) {
        super(game, getParameters(message));
        this.message = message;

    }

    public String getMessage() { return message; }


    public static Map<String,String> getParameters(String message) {
        return Map.of("entity", null, "message", message);
    }
}
