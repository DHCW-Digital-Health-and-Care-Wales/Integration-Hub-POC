package wales.nhs.dhcw.inthub.hl7sender;

public record AppConfig(
    String connectionString,
    String ingressQueueName,
    String serviceBusNamespace,
    String receiverMllpHost,
    int receiverMllpPort,
    boolean useTlsForMLLP,
    String receivingAppId,
    String receivingFacility
) {

    public static AppConfig readEnvConfig() {
        return new AppConfig(
            readEnv("SERVICE_BUS_CONNECTION_STRING", false),
            readEnv("INGRESS_QUEUE_NAME", true),
            readEnv("SERVICE_BUS_NAMESPACE", false),
            readEnv("RECEIVER_MLLP_HOST", true),
            readIntEnv("RECEIVER_MLLP_PORT", true),
            readBooleanEnv("USE_TLS_FOR_MLLP", true),
            readEnv("RECEIVING_APP_ID", false),
            readEnv("RECEIVING_FACILITY", false)
        );
    }

    public boolean isUsingConnectionString() {
        return connectionString != null && !connectionString.isEmpty();
    }

    private static String readEnv(final String name, boolean required) {
        String value = System.getenv(name);
        if (required && (value == null || value.isEmpty())) {
            throw new RuntimeException("Missing required configuration: " + name);
        }
        return value;
    }

    private static boolean readBooleanEnv(final String name, boolean required) {
        String value = readEnv(name, required);
        return Boolean.parseBoolean(value);
    }

    private static int readIntEnv(final String name, boolean required) {
        String value = readEnv(name, required);
        return Integer.parseInt(value);
    }
}

