package edu.missouriwestern.csmp.gg.server.game;

import edu.missouriwestern.csmp.gg.base.*;
import edu.missouriwestern.csmp.gg.base.events.GameStartEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.task.TaskExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MissouriWizardStateUniversityGame extends Game {

    private static Logger logger = Logger.getLogger(MissouriWizardStateUniversityGame.class.getCanonicalName());
    private TaskExecutor taskExecutor;
    private List<EventListener> initialListeners;

    public MissouriWizardStateUniversityGame(DataStore dataStore,
                                             TaskExecutor taskExecutor,
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

    /** loads boards at start of server */
    @org.springframework.context.event.EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        var maps = event.getApplicationContext().getBeansOfType(Board.class);
        for(var mapName : maps.keySet()) {
            this.addBoard(mapName, maps.get(mapName));
            logger.info("loading map " + mapName + ": \n" + maps.get(mapName));
        }
        for(var listener : initialListeners)
            registerListener(listener);
        accept(new GameStartEvent(this));     // indicate game is starting
    }

    /** loads a text file resource as a string */
    public static String loadMap(String mapFileName) throws IOException {
        var mapString = new BufferedReader(new InputStreamReader(
                MissouriWizardStateUniversityGame.class.getClassLoader()
                        .getResourceAsStream(mapFileName)))
                .lines().collect(Collectors.joining("\n"));
        return mapString;
    }
}
