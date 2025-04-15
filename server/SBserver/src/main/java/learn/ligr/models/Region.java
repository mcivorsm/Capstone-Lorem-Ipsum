package learn.ligr.models;

public enum Region {
    NA("NA"),
    EU("EU"),
    OTHER("OTHER"),
    JP("JP");

    private final String displayName;

    Region(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
