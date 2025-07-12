package database;

import database.set.SetStorage;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RDBJava {
    private static volatile RDBJava instance;
    private final Map<String, SetStorage> database = new ConcurrentHashMap<>();

    public static RDBJava getInstance() {
        if (instance == null) {
            synchronized (RDBJava.class) {
                if (instance == null) {
                    instance = new RDBJava();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        System.out.println("Getting value from hash");
        System.out.println(database);
        SetStorage setStorage = database.get(key);
        if (setStorage.getTtl() == -1 || setStorage.getTtl() > Instant.now().toEpochMilli()) {
            return setStorage.getValue();
        } else {
            database.remove(key);
        }

        return "-1";
    }

    public void set(String key, SetStorage value) {
        System.out.println("Setting value in hash");
        database.put(key, value);
    }
}
