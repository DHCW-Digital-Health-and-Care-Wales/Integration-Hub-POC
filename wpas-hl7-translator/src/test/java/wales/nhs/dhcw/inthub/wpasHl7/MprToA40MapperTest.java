package wales.nhs.dhcw.inthub.wpasHl7;


import ca.uhn.hl7v2.HL7Exception;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;
import wales.nhs.dhcw.inthub.wpasHl7.xml.QueueData;

import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MprToA40MapperTest {

    private static final String WPAS_MPI_XML_PATH = "wpas-mpr-a39.xml";
    private static final String EXPECTED_A40_PATH = "wpas-adt_a40.hl7.xml";
    private static final String DUMMY_TEST_TIME = "20250306040503";
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_YMD_FORMAT = "yyyyMMdd";
    public static final String DATETIME_WITH_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Mock
    private DateTimeProvider dateTimeProvider;
    private WpasHl7Translator translator;
    private WpasXmlParser parser;
    private QueueData queueData;

    @BeforeEach
    void setUp() throws JAXBException {
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(DUMMY_TEST_TIME);
        when(dateTimeProvider.getDateFormatter()).thenReturn(DateTimeFormatter.ofPattern(DATE_FORMAT));
        when(dateTimeProvider.getDateTimeFormatter()).thenReturn(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
        when(dateTimeProvider.getDateyyyyMMddFormatter()).thenReturn(DateTimeFormatter.ofPattern(DATE_YMD_FORMAT));
        when(dateTimeProvider.getDateTimeFormatterWithTFormat()).thenReturn(DateTimeFormatter.ofPattern(DATETIME_WITH_T_FORMAT));
        queueData = new QueueData();
        parser = new WpasXmlParser();
        translator = new WpasHl7Translator(dateTimeProvider);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdt39() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        var wpasMessage = parser.parse(TestUtil.getTestFileStream(WPAS_MPI_XML_PATH));
        var expected = TestUtil.getTestFileContent(EXPECTED_A40_PATH);
        queueData.setMaindata(wpasMessage);

        // Act
        var result = translator.translate(queueData);

        // Assert
        TestUtil.assertMatchingExpectedMessage(expected, result);
    }

}
