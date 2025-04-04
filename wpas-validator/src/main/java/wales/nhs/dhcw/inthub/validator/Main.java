package wales.nhs.dhcw.inthub.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.inthub.validator.logging.EventLogger;
import wales.nhs.dhcw.msgbus.ConnectionConfig;
import wales.nhs.dhcw.msgbus.MessageReceiverClient;
import wales.nhs.dhcw.msgbus.MessageSenderClient;
import wales.nhs.dhcw.msgbus.ServiceBusClientFactory;
import wales.nhs.dhcw.inthub.validator.wpas.xml.validator.WpasXmlValidator;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Main class for the application.
 * This class contains the entry point of the application.
 */
public final class Main {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config.properties", ex);
        }
    }

    public static final String APPLICATION_NAME = properties.getProperty("application.name");
    public static final String APPLICATION_VERSION = properties.getProperty("application.version");
    public static final int MAX_BATCH_SIZE = Integer.parseInt(properties.getProperty("max.batch.size"));

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static volatile boolean VALIDATOR_RUNNING = true;

    public static void main(String[] args) throws UnknownHostException, SQLException {
        AppConfig config = AppConfig.readEnvConfig();
        ConnectionConfig clientConfig = new ConnectionConfig(config.connectionString(), config.serviceBusNamespace());
        ServiceBusClientFactory factory = new ServiceBusClientFactory(clientConfig);
        WpasXmlValidator wpasXmlValidator = new WpasXmlValidator();
        EventLogger eventLogger;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the validator");
            VALIDATOR_RUNNING = false;
        }));

        if(config.isUsingLoggingDb()) {
            eventLogger = new EventLogger(
                config.loggingDbUrl(),
                config.loggingDbUsername(),
                config.loggingDbPassword(),
                APPLICATION_NAME,
                APPLICATION_VERSION);
        } else {
            eventLogger = null;
        }

        try (MessageSenderClient senderClient = factory.createMessageSenderClient(config.validatedWpasEgressTopicName());
             MessageReceiverClient receiverClient = factory.createMessageReceiverClient(config.ingressQueueName())) {

            LOGGER.info("Validator started.");

            while (VALIDATOR_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);

                    if(eventLogger != null) {
                        try {
                            eventLogger.logInfoEvent(
                                message.getMessageId(),
                                message.getBody().toStream(),
                                "Received message",
                                "main"
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    var validationResult = wpasXmlValidator.isValid(messageBody.toStream());
                    if(validationResult.success()){
                        senderClient.sendMessage(messageBody);
                    }
                    return validationResult;
                });
            }
        } finally {
            if (eventLogger != null) {
                eventLogger.close();
            }
        }
    }
}

