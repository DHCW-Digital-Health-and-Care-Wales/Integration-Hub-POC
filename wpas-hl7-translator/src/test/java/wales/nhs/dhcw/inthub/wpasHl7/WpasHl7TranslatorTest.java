package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class WpasHl7TranslatorTest {

    public static final String WPAS_MPI_XML_PATH = "wpas-mpi.xml";
    public static final String EXPECTED_A28_PATH = "wpas-adt_a28-msh.hl7.xml";
    public static final int TIME_ASSERTION_WINDOW_IN_MILLISECONDS = 5 * 1000;
    private static final String DUMMY_TEST_TIME = "20000101010101";

    WpasHl7Translator translator;
    private WpasXmlParser parser;

    @BeforeEach
    void setUp() throws JAXBException {
        parser = new WpasXmlParser();
        translator = new WpasHl7Translator();
    }

    @Test
    @Disabled
    void WpasMpiMessage_Is_translatedToAdtA28() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        var wpasXml = getTestFileStream(WPAS_MPI_XML_PATH);
        var expected = getTestFileContent(EXPECTED_A28_PATH);

        // Act
        var result = translator.translate(parser.parse(wpasXml));

        // Assert
        this.assertMessageTimeIsNow(result);
        this.assertMatchingExpectedMessage(expected, result);
    }

    private Reader getTestFileReader(String path) {
        return new InputStreamReader(getTestFileStream(path));
    }

    private static InputStream getTestFileStream(String path) {
        return WpasXmlParserTest.class.getClassLoader().getResourceAsStream(path);
    }

    private String getTestFileContent(String path) {
        return new BufferedReader(getTestFileReader(path))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private void assertMessageTimeIsNow(AbstractMessage actual) throws HL7Exception {
        var msh = (MSH)actual.get("MSH");
        var messageTime  = msh.getMsh7_DateTimeOfMessage().getTs1_Time().getValueAsDate();
        var windowStart = new Date(Calendar.getInstance().getTime().getTime() - TIME_ASSERTION_WINDOW_IN_MILLISECONDS);
        var windowEnd = new Date(Calendar.getInstance().getTime().getTime() + TIME_ASSERTION_WINDOW_IN_MILLISECONDS);

        assertTrue(messageTime.after(windowStart));
        assertTrue(messageTime.before(windowEnd));
    }

    private void assertMatchingExpectedMessage(String expected, AbstractMessage actual)
            throws SAXException, HL7Exception {

        setMessageTimeToDummyValue(actual);

        assertEquals(
                EncodedMessageComparator.standardizeXML(expected),
                EncodedMessageComparator.standardizeXML(encodeHl7Xml(actual))
        );
    }

    private static void setMessageTimeToDummyValue(AbstractMessage actual) throws HL7Exception {
        var msh = (MSH) actual.get("MSH");
        msh.getMsh7_DateTimeOfMessage().getTs1_Time().setValue(DUMMY_TEST_TIME);
    }

    private static String encodeHl7Xml(AbstractMessage result) throws HL7Exception {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getXMLParser();
        String encodedMessage = parser.encode(result);
        return encodedMessage;
    }
}