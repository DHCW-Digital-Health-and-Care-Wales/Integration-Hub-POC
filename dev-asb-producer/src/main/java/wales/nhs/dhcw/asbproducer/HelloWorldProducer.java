package wales.nhs.dhcw.asbproducer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelloWorldProducer {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Queue configuration details.
     */
    private final AsbConfig config;

    /**
     * Create producer for provided configuration.
     * @param asbConfig queue configuration
     */
    public HelloWorldProducer(final AsbConfig asbConfig) {
        this.config = asbConfig;
    }

    /**
     * Connect to the configured queue and read available messages.
     */
    public void produce() {
        LOGGER.debug("Sending message");
        sendMessage("Hello World!");
        LOGGER.debug("Message sent");
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
    }
}
