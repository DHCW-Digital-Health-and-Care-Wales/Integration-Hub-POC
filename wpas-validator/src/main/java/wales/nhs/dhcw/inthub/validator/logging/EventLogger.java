package wales.nhs.dhcw.inthub.validator.logging;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

public class EventLogger implements AutoCloseable {

    private String dbUrl;
    private String username;
    private String password;
    private Connection connection;

    public static final String STORED_PROCEDURE_NAME = "queue.prInsertAPIEvent";
    private static final String SQL_CALL = "{CALL " + STORED_PROCEDURE_NAME + "(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private String hostName;
    private final String apiName;
    private final String apiVersion;

    public EventLogger(String dbUrl, String username, String password, String apiName, String apiVersion) throws UnknownHostException {
        if (isNullOrEmpty(dbUrl) || isNullOrEmpty(username) || isNullOrEmpty(password)) {
            throw new IllegalArgumentException("dbUrl, username and password are required");
        }

        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.apiName = apiName;
        this.apiVersion = apiVersion;

        InetAddress inetAddress = InetAddress.getLocalHost();
        hostName = inetAddress.getHostName();
    }

    public void logEvent(Event event) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl, username, password);
        }

        try (PreparedStatement stmt = connection.prepareStatement(SQL_CALL)) {
            stmt.setString(1, event.eventType().toString());
            stmt.setTimestamp(2, new java.sql.Timestamp(event.eventDateTime()));
            stmt.setString(3, event.eventId());
            stmt.setString(4, apiName);
            stmt.setString(5, apiVersion);
            stmt.setString(6, hostName);
            stmt.setAsciiStream(7, event.eventMessage());
            stmt.setString(8, event.eventInformation());
            stmt.setString(9, event.eventProcess());

            stmt.execute();
        }
    }

    private void logEvent(EventType type, String messageId, InputStream message, String eventInfo, String eventProcess) throws SQLException {
        Event event = new Event(
            type,
            Instant.now().toEpochMilli(),
            messageId,
            message,
            eventInfo,
            eventProcess
        );
        logEvent(event);
    }

    public void logInfoEvent(String messageId, InputStream message, String eventInfo, String eventProcess) throws SQLException {
        logEvent(EventType.INFORMATION, messageId, message, eventInfo, eventProcess);
    }

    public void logWarningEvent(String messageId, InputStream message, String eventInfo, String eventProcess) throws SQLException {
        logEvent(EventType.WARNING, messageId, message, eventInfo, eventProcess);
    }

    public void logErrorEvent(String messageId, InputStream message, String eventInfo, String eventProcess) throws SQLException {
        logEvent(EventType.ERROR, messageId, message, eventInfo, eventProcess);
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
