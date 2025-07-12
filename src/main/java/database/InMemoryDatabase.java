package database;

import database.config.ConfigStorage;
import database.set.SetStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {
    private final Map<String, ConfigStorage> configDatabase = new ConcurrentHashMap<>();
    private final Map<String, SetStorage> setDatabase = new ConcurrentHashMap<>();

    public Map<String, ConfigStorage> getConfigDatabase() {
        return configDatabase;
    }

    public Map<String, SetStorage> getSetDatabase() {
        return setDatabase;
    }
}
