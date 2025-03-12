package wales.nhs.dhcw.msgbus;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSenderClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderClient.class);

    private final ServiceBusSenderClient serviceBusSenderClient;
    private final String topicName;

    public MessageSenderClient(ServiceBusSenderClient serviceBusSenderClient, String topicName) {
        this.serviceBusSenderClient = serviceBusSenderClient;
        this.topicName = topicName;
    }

    public void sendMessage(BinaryData message) {
        serviceBusSenderClient.sendMessage(new ServiceBusMessage(message));
        logger.debug("Message sent successfully to topic: {}", topicName);
    }

    public void sendMessage(String message) {
        sendMessage(BinaryData.fromString(message));
    }

    @Override
    public void close() {
        serviceBusSenderClient.close();
        logger.debug("ServiceBusSenderClient closed.");
    }
}
