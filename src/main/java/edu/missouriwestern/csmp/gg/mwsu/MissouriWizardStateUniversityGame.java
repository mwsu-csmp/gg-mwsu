package edu.missouriwestern.csmp.gg.mwsu;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.mwsu.entities.Player;

import java.util.function.Consumer;
import java.util.logging.Logger;


public class MissouriWizardStateUniversityGame extends Game {

    private static Logger logger = Logger.getLogger(MissouriWizardStateUniversityGame.class.getCanonicalName());
    //spawn
    private Tile spawn; // player spawn

    public MissouriWizardStateUniversityGame(DataStore dataStore,
                                             EventListener eventPropagator,
                                             Consumer<EventListener> incomingEventCallback,
                                             Board ... boards) {
        super(dataStore, eventPropagator, incomingEventCallback, boards);
    }

    @Override
    public void addBoard(Board board) {
        var spawn = board.getTileStream()
                .filter(tile -> tile.getType().equals("player-spawn"))
                .findFirst();
        if (spawn.isPresent()) this.spawn = spawn.get();
        super.addBoard(board);
    }

    public Agent getAgent(String id, String role) {
        if(getAgent(id) != null) return getAgent(id);

        if(role.equals("player")) {
            var player = new Player(this, id);
            addAgent(player);
            this.moveEntity(player, spawn);
            return player;
        }
        return null;
    }
}
