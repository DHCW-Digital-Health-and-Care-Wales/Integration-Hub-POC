package wales.nhs.dhcw.inthub.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.inthub.validator.servicebus.MessageReceiverClient;
import wales.nhs.dhcw.inthub.validator.servicebus.MessageSenderClient;
import wales.nhs.dhcw.inthub.validator.servicebus.ServiceBusClientFactory;

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
    private static volatile boolean PROCESSOR_RUNNING = true;

    public static void main(String[] args) {
        AppConfig config = AppConfig.readEnvConfig();
        ServiceBusClientFactory factory = new ServiceBusClientFactory(config);
        WpasXmlValidator wpasXmlValidator = new WpasXmlValidator();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the validator");
            PROCESSOR_RUNNING = false;
        }));

        try (MessageSenderClient senderClient = factory.createMessageSenderClient();
             MessageReceiverClient receiverClient = factory.createMessageReceiverClient()) {

            LOGGER.info("Validator started.");

            while (PROCESSOR_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);

                    if(wpasXmlValidator.isValid(messageBody.toStream())){
                        senderClient.sendMessage(messageBody);
                        return true;
                    }
                    return false;
                });
            }
        }
    }
}

