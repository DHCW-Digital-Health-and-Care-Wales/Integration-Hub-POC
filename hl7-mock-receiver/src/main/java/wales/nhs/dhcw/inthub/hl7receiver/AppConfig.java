package wales.nhs.dhcw.inthub.hl7receiver;

public record AppConfig(
    String connectionString,
    String serviceBusNamespace,
    String receiverHl7EgressTopicName,
    int receiverMllpPort,
    boolean useTlsForMLLP
) {

    public static AppConfig readEnvConfig() {
        return new AppConfig(
            readEnv("SERVICE_BUS_CONNECTION_STRING", false),
            readEnv("SERVICE_BUS_NAMESPACE", false),
            readEnv("RECEIVER_HL7_EGRESS_TOPIC_NAME", true),
            readIntEnv("RECEIVER_MLLP_PORT", true),
            readBooleanEnv("USE_TLS_FOR_MLLP", true)
        );
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
