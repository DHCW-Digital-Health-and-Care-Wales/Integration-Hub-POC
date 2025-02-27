package wales.nhs.dhcw.asbproducer;

public record AppConfig(
        String connectionString,
        String queueName,
        String message,
        String fileName
) {

    /**
     * Environment variable name for connection string.
     */
    private static final String ENV_CONNECTION_STRING = "CONNECTION_STRING";
    /**
     * Environment variable name for queue name.
     */
    private static final String ENV_QUEUE_NAME = "QUEUE_NAME";
    /**
     * Environment variable name for message to send.
     */
    private static final String ENV_MESSAGE = "MESSAGE";
    /**
     * Environment variable name for fileame to read and send.
     */
    private static final String ENV_FILENAME = "FILENAME";

    /**
     * Read Azure service bus configuration from environment variables.
     * @return configuration
     */
    public static AppConfig readEnvConfig() {
        var config = new AppConfig(
                readEnv(ENV_CONNECTION_STRING),
                readEnv(ENV_QUEUE_NAME),
                readEnv(ENV_MESSAGE),
                readEnv(ENV_FILENAME)
        );
        config.validate();
        return config;
    }

    /**
     * Validate configuration.
     */
    public void validate() {
        if (null == connectionString || connectionString.isEmpty()) {
            throw new RuntimeException("missing configuration: "
                    + ENV_CONNECTION_STRING);
        }

        if (null == queueName || queueName.isEmpty()) {
            throw new RuntimeException("missing configuration: "
                    + ENV_QUEUE_NAME);
        }
        if ((null == message || message.isEmpty())
                && (null == fileName || fileName.isBlank())
        ) {
            throw new RuntimeException("missing configuration: "
                    + ENV_MESSAGE + " or "
                    + ENV_FILENAME);
        }

    }

    private static String readEnv(final String name) {
        return System.getenv(name);
    }
}
