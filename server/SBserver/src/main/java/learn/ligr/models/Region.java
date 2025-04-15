package learn.ligr.models;

public enum Region {
    NA("North America"),
    EU("Europe"),
    OTHER("Other"),
    JP("Japan");

    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
