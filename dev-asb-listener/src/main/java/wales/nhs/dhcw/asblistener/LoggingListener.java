package wales.nhs.dhcw.asblistener;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusFailureReason;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class LoggingListener {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Sleep time after receiving messages.
     * TODO move to the main class
     */
    public static final int END_SLEEP_TIME = 10;
    /**
     * Delay before retry after ServiceBusy error.
     */
    public static final int RETRY_DELAY = 1;

    /**
     * Queue configuration details.
     */
    private final AsbConfig config;

    /**
     * Create listener for provided configuration.
     *
     * @param asbConfig queue configuration
     */
    public LoggingListener(final AsbConfig asbConfig) {
        this.config = asbConfig;
    }

    /**
     * Connect to the configured queue and read available messages.
     */
    public void listen() {
        receiveMessages();
    }

    // handles received messages
    private void receiveMessages() {
        // TODO extract sb client creation to separate factory class
        var processorClient = new ServiceBusClientBuilder()
                .connectionString(config.connectionString())
                .processor()
                .queueName(config.queueName())
                .processMessage(LoggingListener::processMessage)
                .processError(context -> processError(context))
                .buildProcessorClient();

        System.out.println("Starting the processor");
        processorClient.start();

        try {
            TimeUnit.SECONDS.sleep(END_SLEEP_TIME);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception", e);
        }
        System.out.println("Stopping and closing the processor");
        processorClient.close();
    }

    private static void processMessage(
            final ServiceBusReceivedMessageContext context
    ) {
        ServiceBusReceivedMessage message = context.getMessage();
        LOGGER.info(
                "Processing message. Session: {}, Sequence #: {}. Contents: {}",
                message.getMessageId(),
                message.getSequenceNumber(), message.getBody());
    }

    private static void processError(final ServiceBusErrorContext context) {
        LOGGER.error(
                "Error when receiving messages from namespace: '{}'."
                + " Entity: {}",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException()
                instanceof ServiceBusException exception)) {
            LOGGER.error("Non-ServiceBusException occurred",
                    context.getException());
            return;
        }

        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            LOGGER.error(
                            "An unrecoverable error occurred. "
                            + "Stopping processing with reason {}, {}",
                            reason, exception.getMessage());
        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            LOGGER.error("Message lock lost for message: ",
                    context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                TimeUnit.SECONDS.sleep(RETRY_DELAY);
            } catch (InterruptedException e) {
                LOGGER.error("Unable to sleep for period of time");
            }
        } else {
            LOGGER.error("Error source {}, reason {}",
                    context.getErrorSource(),
                    reason, context.getException());
        }
    }
}
