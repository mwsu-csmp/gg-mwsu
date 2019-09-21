package edu.missouriwestern.csmp.gg.server.entities;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.base.events.CommandEvent;
import edu.missouriwestern.csmp.gg.server.events.SpeechEvent;
import edu.missouriwestern.csmp.gg.server.networking.StompClient;

import java.util.Map;
import java.util.logging.Logger;

public class Chest extends Entity implements EventListener, Container {

    private static Logger logger = Logger.getLogger(Guide.class.getCanonicalName());

    private boolean isOpen = false;
    private final String[] messages = {"Opened the Chest"};
    public Chest(Game game, Container startingLocation) {
        super(game, Map.of("sprites", "chest-normal",
                "character", "▣",
                "impassable", "true",
                "description", "a large chest"),
                startingLocation);
    }

    @Override
    public void accept(Event event) {
        if (event instanceof CommandEvent) { // see if someone wants you to talk to them
            var command = (CommandEvent) event;
            if (command.getCommandName().equals("INTERACT")) {
                if (this.isOpen == false) {
                    var player = getGame().getPlayer(command.getProperty("player"));
                    if (player instanceof StompClient) {
                        var avatar = ((StompClient) player).getAvatar();
                        var avatarLocation = getGame().getEntityLocation(avatar);
                        if (avatarLocation instanceof Tile) {
                            var tile = (Tile) avatarLocation;
                            var board = tile.getBoard();
                            var target = board.getAdjacentTile(tile, Direction.valueOf(command.getProperty("parameter")));
                            if (target == getGame().getEntityLocation(this)) {
                                getEntities().forEach(avatar::addEntity);
                                this.isOpen = true;
                                logger.info("chest was opened");
                                getGame().accept(new SpeechEvent(getGame(), this,
                                        messages[(int)(Math.random()*messages.length)]));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getType() {
        return "chest";
    }
}
