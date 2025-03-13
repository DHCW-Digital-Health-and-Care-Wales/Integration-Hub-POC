package wales.nhs.dhcw.inthub.wpasHl7.servicebus;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageSenderClientTest {

    private static final String TOPIC_NAME = "test-topic";

    @Mock
    private ServiceBusSenderClient serviceBusSenderClient;

    private MessageSenderClient messageSenderClient;

    @BeforeEach
    void setUp() {
        messageSenderClient = new MessageSenderClient(serviceBusSenderClient, TOPIC_NAME);
    }

    @Test
    void sendMessage_sends_message_to_serviceBus() {
        // Arrange
        String message = "Test Message";

        // Act
        messageSenderClient.sendMessage(BinaryData.fromString(message));

        // Assert
        verify(serviceBusSenderClient, times(1))
            .sendMessage(argThat(serviceBusMessage -> serviceBusMessage.getBody().toString().equals(message)));
    }

    @Test
    void close_service_bus_sender_client_closed() {
        // Act
        messageSenderClient.close();

        // Assert
        verify(serviceBusSenderClient).close();
    }
}

