package wales.nhs.dhcw.inthub.hl7sender.senderclient;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;
import wales.nhs.dhcw.inthub.hl7sender.AppConfig;
import wales.nhs.dhcw.msgbus.ProcessingResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HL7SenderClientTest {

    private HL7SenderClient hl7SenderClient;

    @Mock
    private AppConfig appConfig;

    private HapiContext context;

    @Mock
    private Initiator initiator;

    @Mock
    private Connection connection;

    @Mock
    private PipeParser pipeParser;

    @Mock
    private XMLParser xmlParser;

    private PipeParser actualPipeParser;

    private static String SOURCE_MESSAGE;


    @BeforeAll()
    static void setupBeforeAll() throws IOException, URISyntaxException {
        SOURCE_MESSAGE = readFile("ADT_A28.xml");
    }

    @BeforeEach
    void setUp() throws Exception {
        context = Mockito.spy(new DefaultHapiContext());
        actualPipeParser = context.getPipeParser();
        when(context.getPipeParser()).thenReturn(pipeParser);
        when(context.getXMLParser()).thenReturn(xmlParser);
    }



    @Test
    void sendMessage_sends_pipe_parsed_message_to_receiver() throws HL7Exception, LLPException, IOException, URISyntaxException, SAXException {
        // Arrange
        mockConnection();
        String expectedMessage = readFile("ADT_A28.txt");
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        when(context.getPipeParser()).thenCallRealMethod();
        when(context.getXMLParser()).thenCallRealMethod();

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        verify(initiator, times(1)).sendAndReceive(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        String actualMessage = capturedMessage.encode();
        assertEquals(
            EncodedMessageComparator.standardize(expectedMessage),
            EncodedMessageComparator.standardize(actualMessage)
        );
    }

    @Test
    void sendMessage_returns_success_response_for_accepted_AA_ACK_response() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String ackMessage = """
            MSH|^~\\&|109|7A1|200|200|20250210112501||ADT^A28^ACK|10928038161|P|2.5.1|||AL\r
            MSA|AA|10928038161\r
            """;
        mockSendAndReceiveResponse(ackMessage);
        ProcessingResult expectedResult = ProcessingResult.successful();

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_success_response_for_accepted_CA_ACK_response() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String ackMessage = """
            MSH|^~\\&|109|7A1|200|200|20250210112501||ADT^A28^ACK|10928038161|P|2.5.1|||AL\r
            MSA|CA|10928038161\r
            """;
        mockSendAndReceiveResponse(ackMessage);
        ProcessingResult expectedResult = ProcessingResult.successful();

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_for_negative_ACK_response() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String ackMessage = """
            MSH|^~\\&|109|7A1|200|200|20250210112501||ADT^A28^ACK|10928038161|P|2.5.1|||AL\r
            MSA|AR|12345|Message rejected\r
            """;
        mockSendAndReceiveResponse(ackMessage);
        ProcessingResult expectedResult = ProcessingResult.failed("Negative ACK received: AR for: 10928038161", true);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_for_unexpected_response() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String ackMessage = """
            MSH|^~\\&|109|7A1|200|200|20250210112501||ADT^A28^ADT_A05|10928038161|P|2.5.1|||AL\r
            EVN||20250210112501|||FSA078103|20250210112500\r
            """;
        mockSendAndReceiveResponse(ackMessage);
        ProcessingResult expectedResult = ProcessingResult.failed("Received a non-ACK message", true);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_xmlParser_parse_throws_HL7Exception() throws HL7Exception {
        // Arrange
        String error = "XML Parsing Error";
        doThrow(new HL7Exception(error)).when(xmlParser).parse(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, false);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_pipeParser_encode_throws_HL7Exception() throws HL7Exception {
        // Arrange
        String error = "Pipe Encoding Error";
        doThrow(new HL7Exception(error)).when(pipeParser).encode(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, false);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_pipeParser_parse_throws_HL7Exception() throws HL7Exception {
        // Arrange
        String error = "Pipe Parsing Error";
        doThrow(new HL7Exception(error)).when(pipeParser).parse(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, false);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_sendAndReceive_throws_HL7Exception() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String error = "Send/Receive Error";
        doThrow(new HL7Exception(error)).when(initiator).sendAndReceive(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, true);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_sendAndReceive_throws_LLPException() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String error = "LLP Communication Error";
        doThrow(new LLPException(error)).when(initiator).sendAndReceive(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, true);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void sendMessage_returns_failed_response_when_sendAndReceive_throws_IOException() throws HL7Exception, LLPException, IOException {
        // Arrange
        mockConnection();
        String error = "IO Error";
        doThrow(new IOException(error)).when(initiator).sendAndReceive(any());
        ProcessingResult expectedResult = ProcessingResult.failed(error, true);

        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        var result = hl7SenderClient.sendMessage(SOURCE_MESSAGE);

        // Assert
        assertResultEqual(expectedResult, result);
    }

    @Test
    void close_connection_closed_when_instantiated() throws HL7Exception {
        // Act
        mockConnection();
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        hl7SenderClient.sendMessage(SOURCE_MESSAGE);
        hl7SenderClient.close();

        // Assert
        verify(connection, times(1)).close();
    }

    @Test
    void close_does_nothing_when_connection_not_instantiated() throws HL7Exception {
        // Act
        hl7SenderClient = new HL7SenderClient(context, appConfig);
        hl7SenderClient.close();

        // Assert
        verify(connection, times(0)).close();
    }

    private static String readFile(String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(HL7SenderClientTest.class.getClassLoader().getResource(fileName).toURI());
        return Files.readString(path);
    }

    private static void assertResultEqual(ProcessingResult expectedResult, ProcessingResult actualResult) {
        assertEquals(expectedResult.success(), actualResult.success());
        assertEquals(expectedResult.errorReason(), actualResult.errorReason());
        assertEquals(expectedResult.retry(), actualResult.retry());
    }

    private void mockSendAndReceiveResponse(String ackMessage) throws HL7Exception, LLPException, IOException {
        Message response = actualPipeParser.parse(ackMessage);
        when(initiator.sendAndReceive(any())).thenReturn(response);
        when(context.getPipeParser()).thenCallRealMethod();
        when(context.getXMLParser()).thenCallRealMethod();
    }

    private void mockConnection() throws HL7Exception {
        doReturn(connection).when(context).newClient(any(), anyInt(), anyBoolean());
        when(connection.getInitiator()).thenReturn(initiator);
    }
}
