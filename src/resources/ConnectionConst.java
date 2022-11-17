package resources;

public enum ConnectionConst {

    DATABASE_DRIVER("com.mysql.cj.jdbc.Driver"),

    DATABASE_HOST("localhost"),
    DATABASE_PORT("3306"),
    DATABASE_NAME("androids_laba"),

    USER("root"),
    PASSWORD("root");

    private final String value;

    ConnectionConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}