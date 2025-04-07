package wales.nhs.dhcw.inthub.validator.logging;

public enum EventType {
    ERROR("Error"),
    INFORMATION("Information"),
    WARNING("Warning");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
