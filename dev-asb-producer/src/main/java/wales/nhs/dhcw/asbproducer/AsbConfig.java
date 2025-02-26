package wales.nhs.dhcw.asbproducer;

public record AsbConfig(
        String connectionString,
        String queueName
) {

    /**
     * Read Azure service bus configuration from environment variables.
     * @return configuration
     */
    public static AsbConfig readEnvConfig() {
        return new AsbConfig(
                readEnv("CONNECTION_STRING"),
                readEnv("QUEUE_NAME")
        );
    }

    private static String readEnv(final String name) {
        String value = System.getenv(name);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException("missing configuration: " + name);
        }
        return value;
    }
}
