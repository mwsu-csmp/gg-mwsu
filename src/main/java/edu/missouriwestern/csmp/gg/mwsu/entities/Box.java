package edu.missouriwestern.csmp.gg.mwsu.entities;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.base.events.EntityMovedEvent;
import edu.missouriwestern.csmp.gg.base.events.TileStateUpdateEvent;
import edu.missouriwestern.csmp.gg.mwsu.events.PushEvent;

import java.util.Map;

public class Box extends Entity implements EventListener {

    public Box(Game game) {
        super(game, Map.of("sprites", "box-normal",
                "character", "☒",
                "impassable", "true",
                "description", "a heavy box"));
    }


    @Override
    public void accept(Event event) {
        if(event instanceof PushEvent) {
            var pushEvent = (PushEvent)event;
            var location = pushEvent.getLocation();
            if(location == getGame().getEntityLocation(this)) {
                // we're being pushed. Check to see if the adjacent location is unoccupied
                var board = location.getBoard();
                var adjacentLocation = board.getAdjacentTile(location, pushEvent.getDirection());
                if(adjacentLocation != null &&
                    !adjacentLocation.hasProperty("impassable") &&
                    adjacentLocation.getEntities()
                            .filter(ent -> ent.hasProperty("impassable"))
                            .count() == 0) {
                    // if so, move the box to that location
                    getGame().moveEntity(this, adjacentLocation);
                }

            }
        } else if(event instanceof EntityMovedEvent &&
            ((EntityMovedEvent)event).getEntity() == this) {
            var location = getGame().getEntityLocation(this);
            // we just moved. Check to see if we've reached a goal condition
            if(location instanceof Tile) {
                var tile = (Tile)location;
                    if(tile.getType().equals("goal")) {
                        this.setProperty("character", "☑");
                        this.setProperty("sprites", "box-in-goal");
                        getGame().accept(new TileStateUpdateEvent(tile));

                        // check for unoccupied goal tile
                        if(!tile.getBoard()// make sure that for all goal tiles, none exist without boxes on them
                                .getTileStream()
                                .filter(t -> t.getType().equals("goal"))// look for goal tiles
                                .filter(t -> t.getEntities()  // without box entities
                                                .filter(ent -> ent.getType().equals("box"))
                                                .count() == 0)
                                .findFirst().isPresent()) {
                            // goal reached, let user through the door
                            tile.getBoard()
                                    .getTileStream()
                                    .filter(t -> t.getType().equals("goal-barrier"))
                                    .forEach(t -> {
                                        t.setProperty("impassable", "false");
                                        t.setProperty("character", " ");
                                        getGame().accept(new TileStateUpdateEvent(t));
                                    });
                        }

                    } else { // reset icon to normal
                        this.setProperty("character", "☒");
                        this.setProperty("sprites", "box-normal");
                        getGame().accept(new TileStateUpdateEvent(tile));
                    }
            }

        }
    }

    @Override
    public String getType() { return "box"; }
}
