package database.config;

import database.BaseStorage;

public class ConfigStorage implements BaseStorage {
    private String key;
    private String value;

    public ConfigStorage(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ConfigStorage(){}

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
