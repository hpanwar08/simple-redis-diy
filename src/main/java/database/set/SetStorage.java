package database.set;

import database.BaseStorage;

public class SetStorage implements BaseStorage {
    private String key;
    private String value;
    private Expiry expiry;
    private long ttl;

    private SetStorage(Builder builder) {
        this.key = builder.key;
        this.value = builder.value;
        this.expiry = builder.expiry;
        this.ttl = builder.ttl;
    }

    public SetStorage(){}

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Expiry getExpiry() {
        return expiry;
    }

    public long getTtl() {
        return ttl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExpiry(Expiry expiry) {
        this.expiry = expiry;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "SetStorage{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", expiry=" + expiry +
                ", expValue=" + ttl +
                '}';
    }

    public static class Builder {
        private String key;
        private String value;
        private Expiry expiry;
        private long ttl;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder expiry(Expiry expiry) {
            this.expiry = expiry;
            return this;
        }

        public Builder expValue(int expValue) {
            this.ttl = expValue;
            return this;
        }

        public SetStorage build() {
            return new SetStorage(this);
        }
    }
}
