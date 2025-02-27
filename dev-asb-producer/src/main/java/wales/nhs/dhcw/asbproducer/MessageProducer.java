package wales.nhs.dhcw.asbproducer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MessageProducer {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Queue configuration details.
     */
    private final AppConfig config;

    /**
     * Create producer for provided configuration.
     * @param appConfig queue configuration
     */
    public MessageProducer(final AppConfig appConfig) {
        this.config = appConfig;
    }

    /**
     * Connect to the configured queue and read available messages.
     */
    public void produce() {
        LOGGER.debug("Sending message");
        sendMessage(getMessage());
        LOGGER.debug("Message sent");
    }

    private String getMessage() {
        if (null != config.message() && !config.message().isEmpty()) {
            LOGGER.info("Sending provided message: {}", config.message());
            return config.message();
        } else if (null != config.fileName() && !config.fileName().isBlank()) {
            LOGGER.info("Sending provided file: {}", config.fileName());
            return readFile();
        }
        throw new RuntimeException("Message not provided");
    }

    private String readFile() {
        try {
            return Files.readString(Path.of(config.fileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(final String text) {
        // TODO extract connection creation to separate factory
        // create a Service Bus Sender client for the queue
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(config.connectionString())
                .sender()
                .queueName(config.queueName())
                .buildClient();

        // send one message to the queue
        senderClient.sendMessage(new ServiceBusMessage(text));
        LOGGER.info("Sent a single message to the queue: {}",
                config.queueName());

        senderClient.close();
    }
}
