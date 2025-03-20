package wales.nhs.dhcw.inthub.hl7sender;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.inthub.hl7sender.senderclient.HL7SenderClient;
import wales.nhs.dhcw.msgbus.ConnectionConfig;
import wales.nhs.dhcw.msgbus.MessageReceiverClient;
import wales.nhs.dhcw.msgbus.ServiceBusClientFactory;

/**
 * Main class for the application.
 * This class contains the entry point of the application.
 */
public final class Main {

    /**
     * Logger instance for the Main class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final int MAX_BATCH_SIZE = 1000;
    private static volatile boolean SENDER_RUNNING = true;

    public static void main(String[] args) throws HL7Exception {
        AppConfig config = AppConfig.readEnvConfig();
        ConnectionConfig clientConfig = new ConnectionConfig(config.connectionString(), config.serviceBusNamespace());
        ServiceBusClientFactory factory = new ServiceBusClientFactory(clientConfig);
        HapiContext context = new DefaultHapiContext();
        HL7SenderClient hl7SenderClient = new HL7SenderClient(context, config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the HL7 sender");
            SENDER_RUNNING = false;
        }));

        try (MessageReceiverClient receiverClient = factory.createMessageReceiverClient(config.ingressQueueName())) {

            LOGGER.info("HL7 sender started.");

            while (SENDER_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);

                    return hl7SenderClient.sendMessage(messageBody.toString());
                });
            }
        }
    }
}

