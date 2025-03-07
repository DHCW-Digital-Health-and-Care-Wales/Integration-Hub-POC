package wales.nhs.dhcw.inthub.validator.servicebus;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.models.DeadLetterOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.inthub.validator.wpas.xml.validator.ValidationResult;

import java.util.function.Function;

public class MessageReceiverClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverClient.class);

    private final ServiceBusReceiverClient serviceBusReceiverClient;

    public MessageReceiverClient(ServiceBusReceiverClient serviceBusReceiverClient) {
        this.serviceBusReceiverClient = serviceBusReceiverClient;
    }

    public void receiveMessages(int numOfMessages, Function<ServiceBusReceivedMessage, ValidationResult> messageValidator) {
        serviceBusReceiverClient.receiveMessages(numOfMessages).forEach(msg -> {
            try {
                var validationResult = messageValidator.apply(msg);

                if (validationResult.success()) {
                    serviceBusReceiverClient.complete(msg);
                    logger.debug("Message processed and completed: {}", msg.getMessageId());
                } else {
                    serviceBusReceiverClient.deadLetter(msg,
                        new DeadLetterOptions()
                            .setDeadLetterReason(validationResult.errorReason().get()));
                    logger.error("Message validation failed, message dead lettered: {}", msg.getMessageId());
                }
            } catch (RuntimeException e) {
                logger.error("Unexpected error validating message: {}", msg.getMessageId(), e);
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
