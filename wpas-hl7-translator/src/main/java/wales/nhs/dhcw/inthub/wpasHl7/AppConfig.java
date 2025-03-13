package wales.nhs.dhcw.inthub.wpasHl7;

public record AppConfig(
    String connectionString,
    String translationQueueName,
    String sendersTopicName,
    String serviceBusNamespace
) {

    /**
     * Reads Azure Service Bus configuration from environment variables.
     * @return configuration
     */
    public static AppConfig readEnvConfig() {
        return new AppConfig(
            readEnv("SERVICE_BUS_CONNECTION_STRING", false),
            readEnv("TRANSLATION_QUEUE_NAME", true),
            readEnv("SENDERS_TOPIC_NAME", true),
            readEnv("SERVICE_BUS_NAMESPACE", false)
        );
    }

    private static String readEnv(final String name, boolean required) {
        String value = System.getenv(name);
        if (required && (value == null || value.isEmpty())) {
            throw new RuntimeException("Missing required configuration: " + name);
        }
        return value;
    }
}

