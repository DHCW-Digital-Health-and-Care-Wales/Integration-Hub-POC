package wales.nhs.dhcw.msgbus;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class MessageSenderClient implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderClient.class);

    private final ServiceBusSenderClient serviceBusSenderClient;
    private final String topicName;

    public MessageSenderClient(ServiceBusSenderClient serviceBusSenderClient, String topicName) {
        this.serviceBusSenderClient = serviceBusSenderClient;
        this.topicName = topicName;
    }

    public void sendMessage(BinaryData messageData) {
        sendMessage(messageData, Collections.emptyMap());
    }

    public void sendMessage(BinaryData messageData,  Map<String, String> customProperties) {
        ServiceBusMessage message = new ServiceBusMessage(messageData);
        message.getApplicationProperties().putAll(customProperties);
        serviceBusSenderClient.sendMessage(message);
        logger.debug("Message sent successfully to topic: {}", topicName);
    }

    public void sendMessage(String messageData) {
        sendMessage(BinaryData.fromString(messageData));
    }

    public void sendMessage(String messageData, Map<String, String> customProperties) {
        sendMessage(BinaryData.fromString(messageData), customProperties);
    }

    @Override
    public void close() {
        serviceBusSenderClient.close();
        logger.debug("ServiceBusSenderClient closed.");
    }
}
