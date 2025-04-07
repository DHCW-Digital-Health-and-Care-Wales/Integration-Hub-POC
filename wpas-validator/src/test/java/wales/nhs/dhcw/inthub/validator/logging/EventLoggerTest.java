package wales.nhs.dhcw.inthub.validator.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventLoggerTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private EventLogger eventLogger;
    private static final Event EVENT = getEvent(EventType.INFORMATION);
    private static final String APPLICATION_NAME = "wpas-validator";
    private static final String APPLICATION_VERSION = "1.0";

    @BeforeEach
    void setUp() throws UnknownHostException {
        eventLogger = spy(new EventLogger("dbUrl;","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION));
    }

    @Test
    void EventLogger_with_invalid_dbUrl_throws_IllegalArgumentException() {
        // Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new EventLogger("","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION);
        });

        // Assert
        assertEquals("dbUrl, username and password are required", thrown.getMessage());
    }

    @Test
    void EventLogger_with_invalid_username_throws_IllegalArgumentException() {
        // Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new EventLogger("dbUrl","","testPass", APPLICATION_NAME, APPLICATION_VERSION);
        });

        // Assert
        assertEquals("dbUrl, username and password are required", thrown.getMessage());
    }

    @Test
    void EventLogger_with_invalid_password_throws_IllegalArgumentException() {
        // Act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new EventLogger("dbUrl","testUser","", APPLICATION_NAME, APPLICATION_VERSION);
        });

        // Assert
        assertEquals("dbUrl, username and password are required", thrown.getMessage());
    }

    @Test
    void logEvent_with_valid_config_create_a_connection() throws SQLException {
        try (MockedStatic driverManager = mockStatic(DriverManager.class)) {
            // Arrange
            setupMocks(driverManager);

            // Act
            eventLogger.logEvent(EVENT);

            // Assert
            driverManager.verify(() -> DriverManager.getConnection("dbUrl;","testUser","testPass"), times(1));
        }
    }

    @Test
    void logEvent_logs_event_to_the_database() throws SQLException, UnknownHostException {
        try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
             MockedStatic<InetAddress> inetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            String expectedHostName = "HostName";
            mockHostName(inetAddress, expectedHostName);
            setupMocks(driverManager);
            eventLogger = spy(new EventLogger("dbUrl;","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION));

            // Act
            eventLogger.logEvent(EVENT);

            // Assert
            verify(connection, times(1)).prepareStatement("{CALL queue.prInsertAPIEvent(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            verify(preparedStatement, times(1)).setString(1, EVENT.eventType().toString());
            verify(preparedStatement, times(1)).setTimestamp(2, new Timestamp(EVENT.eventDateTime()));
            verify(preparedStatement, times(1)).setString(3, EVENT.eventId());
            verify(preparedStatement, times(1)).setString(4, APPLICATION_NAME);
            verify(preparedStatement, times(1)).setString(5, APPLICATION_VERSION);
            verify(preparedStatement, times(1)).setString(6, expectedHostName);
            verify(preparedStatement, times(1)).setAsciiStream(7, EVENT.eventMessage());
            verify(preparedStatement, times(1)).setString(8, EVENT.eventInformation());
            verify(preparedStatement, times(1)).setString(9, EVENT.eventProcess());
            verify(preparedStatement, times(1)).execute();
        }
    }

    @Test
    void logInfoEvent_calls_logEvent_with_correct_event() throws SQLException, UnknownHostException {
        try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
             MockedStatic<InetAddress> inetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            mockHostName(inetAddress, "HostName");
            setupMocks(driverManager);
            ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
            eventLogger = spy(new EventLogger("dbUrl;","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION));

            // Act
            eventLogger.logInfoEvent(EVENT.eventId(), EVENT.eventMessage(), EVENT.eventInformation(), EVENT.eventProcess());

            // Assert
            verifyEventLogCall(eventCaptor, EVENT);
        }
    }

    @Test
    void logWarningEvent_calls_logEvent_with_correct_event() throws SQLException, UnknownHostException {
        try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
             MockedStatic<InetAddress> inetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            mockHostName(inetAddress, "HostName");
            setupMocks(driverManager);
            ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
            eventLogger = spy(new EventLogger("dbUrl;","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION));
            Event event = getEvent(EventType.WARNING);

            // Act
            eventLogger.logWarningEvent(event.eventId(), event.eventMessage(), event.eventInformation(), event.eventProcess());

            // Assert
            verifyEventLogCall(eventCaptor, event);
        }
    }

    @Test
    void logErrorEvent_calls_logEvent_with_correct_event() throws SQLException, UnknownHostException {
        try (MockedStatic<DriverManager> driverManager = mockStatic(DriverManager.class);
             MockedStatic<InetAddress> inetAddress = mockStatic(InetAddress.class)) {
            // Arrange
            mockHostName(inetAddress, "HostName");
            setupMocks(driverManager);
            ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
            eventLogger = spy(new EventLogger("dbUrl;","testUser","testPass", APPLICATION_NAME, APPLICATION_VERSION));
            Event event = getEvent(EventType.ERROR);

            // Act
            eventLogger.logErrorEvent(event.eventId(), event.eventMessage(), event.eventInformation(), event.eventProcess());

            // Assert
            verifyEventLogCall(eventCaptor, event);
        }
    }

    @Test
    void close_closes_connection_if_not_closed() throws SQLException {
        try (MockedStatic driverManager = mockStatic(DriverManager.class)) {
            // Arrange
            setupMocks(driverManager);

            // Act
            eventLogger.logEvent(EVENT);
            eventLogger.close();

            // Assert
            verify(connection, times(1)).close();
        }
    }

    @Test
    void close_does_not_close_connection_if_already_closed() throws SQLException {
        try (MockedStatic driverManager = mockStatic(DriverManager.class)) {
            // Arrange
            setupMocks(driverManager);
            when(connection.isClosed()).thenReturn(true);

            // Act
            eventLogger.logEvent(EVENT);
            eventLogger.close();

            // Assert
            verify(connection, times(0)).close();
        }
    }

    private void verifyEventLogCall(ArgumentCaptor<Event> eventCaptor, Event event) throws SQLException {
        verify(eventLogger, times(1)).logEvent(eventCaptor.capture());
        Event actualEvent = eventCaptor.getValue();
        assertEquals(event.eventType(), actualEvent.eventType());
        assertEquals(event.eventId(), actualEvent.eventId());
        assertEquals(event.eventMessage(), actualEvent.eventMessage());
        assertEquals(event.eventInformation(), actualEvent.eventInformation());
        assertEquals(event.eventProcess(), actualEvent.eventProcess());
        assertTrue(actualEvent.eventDateTime() > 0);
    }

    private static void mockHostName(MockedStatic<InetAddress> inetAddress, String hostName) {
        InetAddress mockInetAddress = mock(InetAddress.class);
        inetAddress.when(InetAddress::getLocalHost).thenReturn(mockInetAddress);
        when(mockInetAddress.getHostName()).thenReturn(hostName);
    }
    
    private void setupMocks(MockedStatic driverManager) throws SQLException {
        driverManager.when(() -> DriverManager.getConnection(any(), any(), any())).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    private static Event getEvent(EventType eventType) {
        return new Event(
            eventType,
            1743604502626L,
            "1234",
            new ByteArrayInputStream("wpas message".getBytes(StandardCharsets.UTF_8)),
            "message received",
            "main"
        );
    }
}
