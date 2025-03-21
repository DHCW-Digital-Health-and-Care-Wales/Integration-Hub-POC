package wales.nhs.dhcw.inthub.hl7receiver;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wales.nhs.dhcw.msgbus.MessageSenderClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HL7MessageHandlerTest {

    @Mock
    private PipeParser pipeParser;
    @Mock
    private HapiContext context;
    @Mock
    private Message message;
    @Mock
    private MessageSenderClient messageSenderClient;

    private HL7MessageHandler messageHandler;


    @BeforeEach
    void setUp() {
        when(context.getPipeParser()).thenReturn(pipeParser);
        messageHandler = new HL7MessageHandler(context, messageSenderClient);
    }

    @Test
    void processMessage_returns_ack_and_sends_message_to_topic() throws HL7Exception, IOException {
        // Arrange
        var expectedMessage = "HL7 Message";
        when(pipeParser.encode(message)).thenReturn(expectedMessage);
        when(message.generateACK()).thenReturn(message);

        // Act
        Message response = messageHandler.processMessage(message, Map.of());

        // Assert
        verify(pipeParser).encode(message);
        verify(message).generateACK();
        assertEquals(message, response);
        verify(messageSenderClient).sendMessage(expectedMessage);
    }

    @Test
    void processMessage_throws_exception_on_error() throws HL7Exception, IOException {
        // Arrange
        when(pipeParser.encode(message)).thenReturn("HL7 Message");
        when(message.generateACK()).thenThrow(new IOException("IO error"));

        // Act
        HL7Exception thrown = assertThrows(HL7Exception.class, () -> {
            messageHandler.processMessage(message, Map.of());
        });

        // Assert
        assertEquals("java.io.IOException: IO error", thrown.getMessage());
    }

    @Test
    void canProcess_always_returns_true() {
        assertTrue(messageHandler.canProcess(message));
    }
}
