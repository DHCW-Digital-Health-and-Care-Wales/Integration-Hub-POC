package wales.nhs.dhcw.inthub.wpasHl7.servicebus;

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
        ServiceBusMessage ServiceMessage = new ServiceBusMessage(message);
        ServiceMessage.setContentType("application/xml");
        serviceBusSenderClient.sendMessage(ServiceMessage);
        logger.debug("Message sent successfully to topic: {}", topicName);
    }

    @Override
    public void close() {
        serviceBusSenderClient.close();
        logger.debug("ServiceBusSenderClient closed.");
    }
}
