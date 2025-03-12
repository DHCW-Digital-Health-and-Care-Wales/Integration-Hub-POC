package wales.nhs.dhcw.inthub.wpasHl7;

import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.msgbus.*;

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

    public static void main(String[] args) throws JAXBException {
        AppConfig config = AppConfig.readEnvConfig();
        ConnectionConfig msgBusConfig = new ConnectionConfig(config.connectionString(), config.fullyQualifiedNamespace());
        ServiceBusClientFactory factory = new ServiceBusClientFactory(msgBusConfig);
        WpasXmlParser parser = new WpasXmlParser();
        WpasHl7Translator translator = new WpasHl7Translator(new DateTimeProvider());
        Hl7Encoder hl7Encoder = new Hl7Encoder();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the validator");
            VALIDATOR_RUNNING = false;
        }));

        try (MessageSenderClient senderClient = factory.createMessageSenderClient(config.sendersTopicName());
             MessageReceiverClient receiverClient = factory.createMessageReceiverClient(config.translationQueueName())) {

            LOGGER.info("Validator started.");

            while (VALIDATOR_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);

                    try {
                        var mainData = parser.parse(messageBody.toStream());
                        var hl7Data = translator.translate(mainData);
                        var serializedHl7 = hl7Encoder.encode(hl7Data);
                        senderClient.sendMessage(serializedHl7);

                    } catch (Exception e) {
                        LOGGER.error("WPAS message translation error", e);
                        return ProcessingResult.failed(e.getMessage());
                    }

                    return ProcessingResult.successful();
                });
            }
        }
    }
}

