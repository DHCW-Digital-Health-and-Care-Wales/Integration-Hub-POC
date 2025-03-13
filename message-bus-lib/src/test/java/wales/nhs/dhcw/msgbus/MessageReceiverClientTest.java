package wales.nhs.dhcw.msgbus;

import com.azure.core.util.IterableStream;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.models.DeadLetterOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
class MessageReceiverClientTest {

    @Mock
    private ServiceBusReceiverClient serviceBusReceiverClient;

    private MessageReceiverClient messageReceiverClient;

    @BeforeEach
    void setUp() {
        messageReceiverClient = new MessageReceiverClient(serviceBusReceiverClient);
    }

    private static ServiceBusReceivedMessage createMessage(String messageId) {
        ServiceBusReceivedMessage message = Mockito.mock(ServiceBusReceivedMessage.class);
        Mockito.when(message.getMessageId()).thenReturn(messageId);
        return message;
    }

    @Test
    void receiveMessages_calls_complete_when_valid_message() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        Mockito.when(serviceBusReceiverClient.receiveMessages(ArgumentMatchers.anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        Function<ServiceBusReceivedMessage, ProcessingResult> validator = msg ->
            new ProcessingResult(true, Optional.empty());

        // Act
        messageReceiverClient.receiveMessages(1, validator);

        // Assert
        Mockito.verify(serviceBusReceiverClient, Mockito.times(1)).complete(message);
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).abandon(message);
    }



    @Test
    void receiveMessages_multiple_messages_calls_complete_when_valid_message_and_abandon_when_invalid() {
        // Arrange
        ServiceBusReceivedMessage message1 = createMessage("123");
        ServiceBusReceivedMessage message2 = createMessage("456");
        String deadLetterReason = "XML Validation Error";

        Mockito.when(serviceBusReceiverClient.receiveMessages(ArgumentMatchers.anyInt())).thenReturn(new IterableStream<>(List.of(message1, message2)));

        Function<ServiceBusReceivedMessage, ProcessingResult> validator = msg -> {
            if ("123".equals(msg.getMessageId())) {
                return new ProcessingResult("123".equals(msg.getMessageId()), Optional.empty());
            }
            return new ProcessingResult(false, Optional.of(deadLetterReason));
        };

        // Act
        messageReceiverClient.receiveMessages(2, validator);

        // Assert
        Mockito.verify(serviceBusReceiverClient, Mockito.times(1)).complete(message1);
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).abandon(message1);
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).complete(message2);
        ArgumentCaptor<DeadLetterOptions> captor = ArgumentCaptor.forClass(DeadLetterOptions.class);
        Mockito.verify(serviceBusReceiverClient, Mockito.times(1)).deadLetter(ArgumentMatchers.eq(message2), captor.capture());

        // Assert captured values
        DeadLetterOptions capturedOptions = captor.getValue();
        Assertions.assertEquals(deadLetterReason, capturedOptions.getDeadLetterReason());
    }

    @Test
    void receiveMessages_calls_deadLetter_when_message_fails_validation() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        Mockito.when(serviceBusReceiverClient.receiveMessages(ArgumentMatchers.anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        String deadLetterReason = "XML Validation Error";
        Function<ServiceBusReceivedMessage, ProcessingResult> validator = msg ->
            new ProcessingResult(false, Optional.of(deadLetterReason));

        // Act
        messageReceiverClient.receiveMessages(1, validator);

        // Assert
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).complete(message);
        ArgumentCaptor<DeadLetterOptions> captor = ArgumentCaptor.forClass(DeadLetterOptions.class);
        Mockito.verify(serviceBusReceiverClient, Mockito.times(1)).deadLetter(ArgumentMatchers.eq(message), captor.capture());

        // Assert captured values
        DeadLetterOptions capturedOptions = captor.getValue();
        Assertions.assertEquals(deadLetterReason, capturedOptions.getDeadLetterReason());
    }

    @Test
    void receiveMessages_calls_abandon_when_exception_during_validation() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        Mockito.when(serviceBusReceiverClient.receiveMessages(ArgumentMatchers.anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        Function<ServiceBusReceivedMessage, ProcessingResult> validator = msg -> {
            throw new RuntimeException("Test Exception");
        };

        // Act
        messageReceiverClient.receiveMessages(1, validator);

        // Assert
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).complete(message);
        Mockito.verify(serviceBusReceiverClient, Mockito.times(1)).abandon(message);
    }

    @Test
    void receiveMessages_makes_no_calls_when_no_messages_received() {
        // Arrange
        Mockito.when(serviceBusReceiverClient.receiveMessages(ArgumentMatchers.anyInt())).thenReturn(new IterableStream<>(List.of()));

        Function<ServiceBusReceivedMessage, ProcessingResult> validator = msg ->
            new ProcessingResult(true, Optional.empty());

        // Act
        messageReceiverClient.receiveMessages(1, validator);

        // Assert
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).complete(ArgumentMatchers.any());
        Mockito.verify(serviceBusReceiverClient, Mockito.never()).abandon(ArgumentMatchers.any());
    }

    @Test
    void close_service_bus_receiver_client_closed() {
        // Act
        messageReceiverClient.close();

        // Assert
        Mockito.verify(serviceBusReceiverClient).close();
    }
}
