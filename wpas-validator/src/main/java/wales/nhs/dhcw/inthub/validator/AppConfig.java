package wales.nhs.dhcw.inthub.validator;

public record AppConfig(
    String connectionString,
    String ingressQueueName,
    String validatedWpasEgressTopicName,
    String serviceBusNamespace,
    String loggingDbUrl,
    String loggingDbUsername,
    String loggingDbPassword
) {

    /**
     * Reads Azure Service Bus configuration from environment variables.
     * @return configuration
     */
    public static AppConfig readEnvConfig() {
        return new AppConfig(
            readEnv("SERVICE_BUS_CONNECTION_STRING", false),
            readEnv("INGRESS_QUEUE_NAME", true),
            readEnv("VALIDATED_WPAS_EGRESS_TOPIC_NAME", true),
            readEnv("SERVICE_BUS_NAMESPACE", false),
            readEnv("LOGGING_DB_URL", false),
            readEnv("LOGGING_DB_USERNAME", false),
            readEnv("LOGGING_DB_PASSWORD", false)
        );
    }

    public boolean isUsingConnectionString() {
        return connectionString != null && !connectionString.isEmpty();
    }

    public boolean isUsingLoggingDb() {
        return loggingDbUrl != null && !loggingDbUrl.isEmpty();
    }

    private static String readEnv(final String name, boolean required) {
        String value = System.getenv(name);
        if (required && (value == null || value.isEmpty())) {
            throw new RuntimeException("Missing required configuration: " + name);
        }
        return value;
    }
}

