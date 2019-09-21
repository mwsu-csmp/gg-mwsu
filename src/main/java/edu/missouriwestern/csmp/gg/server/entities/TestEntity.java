package edu.missouriwestern.csmp.gg.server.entities;

import edu.missouriwestern.csmp.gg.base.Entity;
import edu.missouriwestern.csmp.gg.base.Game;

import java.util.Map;

public class TestEntity extends Entity {

    protected TestEntity(Game game, Map<String, String> properties) {
        super(game, properties);
    }

    @Override
    public String getType() {
        return null;
    }
}
