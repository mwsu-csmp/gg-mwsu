package edu.missouriwestern.csmp.gg.server.entities;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.base.events.CommandEvent;
import edu.missouriwestern.csmp.gg.server.events.FlagEvent;
import edu.missouriwestern.csmp.gg.server.networking.StompClient;

import java.util.logging.Logger;


public class LockedDoor implements EventListener{
    private static Logger logger = Logger.getLogger(LockedDoor.class.getCanonicalName());
    private final String[] messages = {"Unlocked the door"};
    private boolean locked = true;
    private Game game;
    private Tile tile;
    private Entity key;


    public LockedDoor(Game game, Tile tile, Entity key) {
        tile.setProperty("impassable", "true");
        this.game=game;
        this.tile=tile;
        this.key=key;

        game.registerListener(this);
    }

    @Override
    public void accept(Event event) {
        if(event instanceof CommandEvent) {
            var command = (CommandEvent)event;
            if(command.getCommandName().equals("INTERACT")) {
                logger.info("Interact was pressed");
                var player = game.getPlayer(command.getProperty("player"));
                if (player instanceof StompClient) { // TODO: this cast sucks, shouldn't be tied to client, rethink approach
                    var avatar = ((StompClient) player).getAvatar();
                    var avatarLocation = game.getEntityLocation(avatar);
                    if (avatarLocation instanceof Tile) {
                        var avatarTile = (Tile) avatarLocation;
                        var board = avatarTile.getBoard();
                        var target = board.getAdjacentTile(avatarTile, Direction.valueOf(command.getProperty("parameter")));
                        logger.info("INSIDE the LOCKED DOOR| targer" +target+"  |tile: "+this.tile);

                        if (target == this.tile) { // someone is interacting with us

                            if (avatar.containsEntity(this.key)){
                                locked = false;
                                this.tile.setProperty("impassable", "false");
                                game.accept(new FlagEvent(game,"Unlocked"));
                            }





                        }
                    }
                }


            }
        }


    }


}


