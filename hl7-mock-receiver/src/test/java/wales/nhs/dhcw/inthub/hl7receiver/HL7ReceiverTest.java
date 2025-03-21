package wales.nhs.dhcw.inthub.hl7receiver;

import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HL7ReceiverTest {

    @Mock
    private HapiContext context;

    @Mock
    private HL7Service server;

    @Mock
    private AppConfig config;

    private HL7Receiver hl7Receiver;

    @BeforeEach
    void setUp() {
        when(context.newServer(anyInt(), anyBoolean())).thenReturn(server);
        when(config.receiverMllpPort()).thenReturn(2575);
        when(config.useTlsForMLLP()).thenReturn(false);

        hl7Receiver = new HL7Receiver(context, config);
    }

    @Test
    void HL7Receiver_creates_server() {
        // Assert
        verify(context).newServer(2575, false);
    }

    @Test
    void registerHandler_registers_handler_with_server() {
        // Arrange
        var handler = mock(HL7MessageHandler.class);

        // Act
        hl7Receiver.registerHandler(handler);

        // Assert
        verify(server).registerApplication(any(), any(), same(handler));
    }

    @Test
    void startServer_starts_server() throws InterruptedException {
        // Act
        hl7Receiver.startServer();

        // Assert
        verify(server).startAndWait();
    }

    @Test
    void close_stops_server() {
        // Act
        hl7Receiver.close();

        // Assert
        verify(server).stopAndWait();
    }
}
