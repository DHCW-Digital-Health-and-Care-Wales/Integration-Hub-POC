package wales.nhs.dhcw.inthub.hl7receiver;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.msgbus.ConnectionConfig;
import wales.nhs.dhcw.msgbus.MessageSenderClient;
import wales.nhs.dhcw.msgbus.ServiceBusClientFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Main class for the application.
 * This class contains the entry point of the application.
 */
public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        AppConfig config = AppConfig.readEnvConfig();
        ConnectionConfig clientConfig = new ConnectionConfig(config.connectionString(), config.serviceBusNamespace());
        ServiceBusClientFactory factory = new ServiceBusClientFactory(clientConfig);
        HapiContext context = new DefaultHapiContext();

        CountDownLatch latch = new CountDownLatch(1);

        try (MessageSenderClient senderClient = factory.createMessageSenderClient(config.receiverHl7EgressTopicName());
            HL7Receiver hl7Receiver = new HL7Receiver(context, config)) {

            HL7MessageHandler handler = new HL7MessageHandler(context, senderClient);
            hl7Receiver.registerHandler(handler);
            hl7Receiver.startServer();
            LOGGER.info("HL7 receiver started.");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutdown signal received. Cleaning up...");
                latch.countDown(); // Release latch to allow main method to exit
            }));

            latch.await(); // Block main thread until latch is released
        }
    }
}
