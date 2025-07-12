package handler;

public enum DataType {
    ARRAY("*"),
    BULK_STRING("$"),
    INTEGER(":"),
    STRING("+"),
    ERR("-"),
    UNKNOWN("");

    private final String type;

    DataType(String type) {
        this.type = type;
    }

    public static DataType fromType(String dataType) {
        for (DataType d : DataType.values()) {
            if (d.getType().equals(dataType)) return d;
        }
        return UNKNOWN;
    }

    public String getType() {
        return type;
    }
}
