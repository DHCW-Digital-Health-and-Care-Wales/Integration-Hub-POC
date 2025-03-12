package wales.nhs.dhcw.inthub.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.msgbus.ConnectionConfig;
import wales.nhs.dhcw.msgbus.MessageReceiverClient;
import wales.nhs.dhcw.msgbus.MessageSenderClient;
import wales.nhs.dhcw.msgbus.ServiceBusClientFactory;
import wales.nhs.dhcw.inthub.validator.wpas.xml.validator.WpasXmlValidator;

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
    private static volatile boolean VALIDATOR_RUNNING = true;

    public static void main(String[] args) {
        AppConfig config = AppConfig.readEnvConfig();
        ConnectionConfig clientConfig = new ConnectionConfig(config.connectionString(), config.fullyQualifiedNamespace());
        ServiceBusClientFactory factory = new ServiceBusClientFactory(clientConfig);
        WpasXmlValidator wpasXmlValidator = new WpasXmlValidator();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the validator");
            VALIDATOR_RUNNING = false;
        }));

        try (MessageSenderClient senderClient = factory.createMessageSenderClient(config.validatedWpasEgressTopicName());
             MessageReceiverClient receiverClient = factory.createMessageReceiverClient(config.ingressQueueName())) {

            LOGGER.info("Validator started.");

            while (VALIDATOR_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);

                    var validationResult = wpasXmlValidator.isValid(messageBody.toStream());
                    if(validationResult.success()){
                        senderClient.sendMessage(messageBody);
                    }
                    return validationResult;
                });
            }
        }
    }
}

