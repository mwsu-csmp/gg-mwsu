package edu.missouriwestern.csmp.gg.mwsu.game;

import edu.missouriwestern.csmp.gg.base.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Logger;


public class MissouriWizardStateUniversityGame extends Game {

    private static Logger logger = Logger.getLogger(MissouriWizardStateUniversityGame.class.getCanonicalName());
    private Executor taskExecutor;
    private List<EventListener> initialListeners;

    public MissouriWizardStateUniversityGame(DataStore dataStore,
                                             Executor taskExecutor,
                                             EventListener ... listeners)
            throws Exception {
        super(dataStore);
        this.taskExecutor = taskExecutor;
        this.initialListeners = Arrays.asList(listeners);
    }

    @Override
    public void addEntity(Entity ent) {
        super.addEntity(ent);
        if(ent instanceof Runnable) {
            logger.info("starting thread for executable entity " + ent.getID());
            taskExecutor.execute((Runnable)ent);
        }
    }

}
