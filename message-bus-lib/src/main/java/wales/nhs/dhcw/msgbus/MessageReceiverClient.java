package wales.nhs.dhcw.msgbus;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.models.DeadLetterOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;


public class MessageReceiverClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverClient.class);

    private final ServiceBusReceiverClient serviceBusReceiverClient;

    public MessageReceiverClient(ServiceBusReceiverClient serviceBusReceiverClient) {
        this.serviceBusReceiverClient = serviceBusReceiverClient;
    }

    public void receiveMessages(int numOfMessages, Function<ServiceBusReceivedMessage, ProcessingResult> messageProcessor) {
        serviceBusReceiverClient.receiveMessages(numOfMessages).forEach(msg -> {
            try {
                var result = messageProcessor.apply(msg);

                if (result.success()) {
                    serviceBusReceiverClient.complete(msg);
                    logger.debug("Message processed and completed: {}", msg.getMessageId());
                } else if (result.retry().orElse(false)) {
                    logger.error("Message processing failed, message abandoned: {}", msg.getMessageId());
                    serviceBusReceiverClient.abandon(msg);
                } else {
                    serviceBusReceiverClient.deadLetter(msg,
                        new DeadLetterOptions()
                            .setDeadLetterReason(result.errorReason().get()));
                    logger.error("Message processing failed, message dead lettered: {}", msg.getMessageId());
                }
            } catch (RuntimeException e) {
                logger.error("Unexpected error processing message: {}", msg.getMessageId(), e);
                serviceBusReceiverClient.abandon(msg);
            }
        });
    }

    @Override
    public void close() {
        serviceBusReceiverClient.close();
        logger.debug("ServiceBusReceiverClient closed.");
    }
}
