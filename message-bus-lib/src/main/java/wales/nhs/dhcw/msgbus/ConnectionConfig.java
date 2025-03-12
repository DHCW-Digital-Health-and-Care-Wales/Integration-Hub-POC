package wales.nhs.dhcw.msgbus;

public record ConnectionConfig(
    String connectionString,
    String fullyQualifiedNamespace
) {

    public boolean isUsingConnectionString() {
        return connectionString != null && !connectionString.isEmpty();
    }
}

