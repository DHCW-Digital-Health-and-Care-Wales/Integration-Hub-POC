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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        when(message.getMessageId()).thenReturn(messageId);
        return message;
    }

    @Test
    void receiveMessages_calls_complete_when_valid_message() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg ->
            ProcessingResult.successful();

        // Act
        messageReceiverClient.receiveMessages(1, processor);

        // Assert
        verify(serviceBusReceiverClient, times(1)).complete(message);
        verify(serviceBusReceiverClient, never()).abandon(message);
    }

    @Test
    void receiveMessages_multiple_messages_calls_complete_when_valid_message_and_abandon_when_invalid() {
        // Arrange
        ServiceBusReceivedMessage message1 = createMessage("123");
        ServiceBusReceivedMessage message2 = createMessage("456");
        String deadLetterReason = "XML processing Error";

        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of(message1, message2)));

        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg -> {
            if ("123".equals(msg.getMessageId())) {
                return ProcessingResult.successful();
            }
            return ProcessingResult.failed(deadLetterReason, false);
        };

        // Act
        messageReceiverClient.receiveMessages(2, processor);

        // Assert
        verify(serviceBusReceiverClient, times(1)).complete(message1);
        verify(serviceBusReceiverClient, never()).abandon(message1);
        verify(serviceBusReceiverClient, never()).complete(message2);
        ArgumentCaptor<DeadLetterOptions> captor = ArgumentCaptor.forClass(DeadLetterOptions.class);
        verify(serviceBusReceiverClient, times(1)).deadLetter(eq(message2), captor.capture());

        // Assert captured values
        DeadLetterOptions capturedOptions = captor.getValue();
        assertEquals(deadLetterReason, capturedOptions.getDeadLetterReason());
    }

    @Test
    void receiveMessages_calls_deadLetter_when_message_fails_processing() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        String deadLetterReason = "XML processing Error";
        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg ->
            ProcessingResult.failed(deadLetterReason, false);

        // Act
        messageReceiverClient.receiveMessages(1, processor);

        // Assert
        verify(serviceBusReceiverClient, never()).complete(message);
        ArgumentCaptor<DeadLetterOptions> captor = ArgumentCaptor.forClass(DeadLetterOptions.class);
        verify(serviceBusReceiverClient, times(1)).deadLetter(eq(message), captor.capture());

        // Assert captured values
        DeadLetterOptions capturedOptions = captor.getValue();
        assertEquals(deadLetterReason, capturedOptions.getDeadLetterReason());
    }

    @Test
    void receiveMessages_calls_abandon_when_message_fails_processing_with_retry() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg ->
            ProcessingResult.failed("Pipe Parsing Error", true);

        // Act
        messageReceiverClient.receiveMessages(1, processor);

        // Assert
        verify(serviceBusReceiverClient, never()).complete(message);
        verify(serviceBusReceiverClient, times(1)).abandon(message);
    }

    @Test
    void receiveMessages_calls_abandon_when_exception_during_processing() {
        // Arrange
        ServiceBusReceivedMessage message = createMessage("123");

        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of(message)));

        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg -> {
            throw new RuntimeException("Test Exception");
        };

        // Act
        messageReceiverClient.receiveMessages(1, processor);

        // Assert
        verify(serviceBusReceiverClient, never()).complete(message);
        verify(serviceBusReceiverClient, times(1)).abandon(message);
    }

    @Test
    void receiveMessages_makes_no_calls_when_no_messages_received() {
        // Arrange
        when(serviceBusReceiverClient.receiveMessages(anyInt())).thenReturn(new IterableStream<>(List.of()));

        Function<ServiceBusReceivedMessage, ProcessingResult> processor = msg ->
            ProcessingResult.successful();

        // Act
        messageReceiverClient.receiveMessages(1, processor);

        // Assert
        verify(serviceBusReceiverClient, never()).complete(any());
        verify(serviceBusReceiverClient, never()).abandon(any());
    }

    @Test
    void close_service_bus_receiver_client_closed() {
        // Act
        messageReceiverClient.close();

        // Assert
        verify(serviceBusReceiverClient).close();
    }
}
