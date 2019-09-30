package edu.missouriwestern.csmp.gg.mwsu;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.mwsu.entities.Player;

import java.util.function.Consumer;
import java.util.logging.Logger;


public class MissouriWizardStateUniversityGame extends Game {

    private static Logger logger = Logger.getLogger(MissouriWizardStateUniversityGame.class.getCanonicalName());

    private Tile spawn; // player spawn
    private final String initialMapName;

    public MissouriWizardStateUniversityGame(DataStore dataStore,
                                             EventListener eventPropagator,
                                             Consumer<EventListener> incomingEventCallback,
                                             String initialMapName) {
        super(dataStore, eventPropagator, incomingEventCallback);
        this.initialMapName = initialMapName;
    }

    @Override
    public void addBoard(String boardId, Board board) {
        if (boardId.equals(initialMapName)) {
            var spawn = board.getTileStream()
                    .filter(tile -> tile.getType().equals("player-spawn"))
                    .findFirst();
            if (spawn.isPresent()) this.spawn = spawn.get();
        }
        super.addBoard(boardId, board);
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
