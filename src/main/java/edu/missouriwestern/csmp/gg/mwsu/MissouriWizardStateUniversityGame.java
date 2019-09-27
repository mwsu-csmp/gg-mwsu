package edu.missouriwestern.csmp.gg.mwsu;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.mwsu.entities.Player;

import java.util.function.Consumer;
import java.util.logging.Logger;


public class MissouriWizardStateUniversityGame extends Game {

    private static Logger logger = Logger.getLogger(MissouriWizardStateUniversityGame.class.getCanonicalName());

    public MissouriWizardStateUniversityGame(DataStore dataStore,
                                             EventListener eventPropagator,
                                             Consumer<EventListener> incomingEventCallback) {
        super(dataStore, eventPropagator, incomingEventCallback);
    }

    public Agent getAgent(String id, String role) {
        if(getAgent(id) != null) return getAgent(id);

        if(role.equals("player")) {
            var player = new Player(this, id);
            addAgent(player);
            return player;
        }
        return null;
    }
}
