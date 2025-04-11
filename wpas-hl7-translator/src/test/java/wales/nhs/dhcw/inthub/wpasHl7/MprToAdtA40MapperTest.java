package wales.nhs.dhcw.inthub.wpasHl7;


import ca.uhn.hl7v2.HL7Exception;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;
import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;

import java.time.Instant;
import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MprToAdtA40MapperTest {

    private static final String WPAS_MPI_XML_PATH = "wpas-mpr-a39.xml";
    private static final String EXPECTED_A40_PATH = "wpas-adt_a40.hl7.xml";
    private static final String DUMMY_TEST_TIME = "20250206085438";
    private static final String TEST_TIME = "2025-03-26T12:22:27Z";

    @Mock
    private DateTimeProvider dateTimeProvider;
    private WpasHl7Translator translator;
    private WpasXmlParser parser;
    private WpasData queueData;

    @BeforeEach
    void setUp() throws JAXBException {
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(DUMMY_TEST_TIME);

        parser = new WpasXmlParser();
        queueData = new WpasData();
        translator = new WpasHl7Translator(dateTimeProvider);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdt39() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        var wpasMessage = parser.parse(TestUtil.getTestFileStream(WPAS_MPI_XML_PATH));
        var expected = TestUtil.getTestFileContent(EXPECTED_A40_PATH);
        Date dateTime = Date.from(Instant.parse(TEST_TIME));
        queueData.setMaindata(wpasMessage);
        queueData.setQueueDateTime(dateTime);

        // Act
        var result = translator.translate(queueData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }

}
