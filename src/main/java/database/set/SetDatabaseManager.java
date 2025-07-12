package database.set;

import database.InMemoryDatabase;

import java.time.Instant;
import java.util.List;

public class SetDatabaseManager {
    private final InMemoryDatabase inMemoryDatabase;

    public SetDatabaseManager(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public void setValue(List<Object> data) {
        String key = (String) data.get(1);
        String value = (String) data.get(2);
        Expiry expire = Expiry.valueOf("PX");
        long ttl = -1;

        SetStorage setStorage = new SetStorage();
        setStorage.setKey(key);
        setStorage.setValue(value);
        setStorage.setExpiry(expire);
        setStorage.setTtl(ttl);

        if (data.size() == 5) {
            String expireKey = (String) data.get(3);
            int exp = Integer.parseInt((String) data.get(4));
            if (expireKey.equalsIgnoreCase("px")) {
                ttl = Instant.now().plusMillis(exp).toEpochMilli();
            }
            setStorage.setExpiry(Expiry.valueOf(expireKey.toUpperCase()));
            setStorage.setTtl(ttl);
        }

        inMemoryDatabase.getSetDatabase().put(key, setStorage);
    }

    public String getValue(String key) {
        System.out.println("Getting value from hash");
        SetStorage setStorage = inMemoryDatabase.getSetDatabase().get(key);
        if (setStorage.getTtl() == -1 || setStorage.getTtl() > Instant.now().toEpochMilli()) {
            return setStorage.getValue();
        } else {
            inMemoryDatabase.getSetDatabase().remove(key);
        }
        return "-1";
    }
}
