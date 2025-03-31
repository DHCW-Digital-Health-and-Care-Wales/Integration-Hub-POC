package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.HL7Exception;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MpaToAdtA31MapperTest {

    private static final String WPAS_MPI_XML_PATH = "wpas-mpa.xml";
    private static final String EXPECTED_A28_PATH = "wpas-adt_a31.hl7.xml";
    private static final String DUMMY_TEST_TIME = "20250210145216";

    @Mock
    private DateTimeProvider dateTimeProvider;
    private WpasHl7Translator translator;
    private WpasXmlParser parser;

    @BeforeEach
    void setUp() throws JAXBException {
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(DUMMY_TEST_TIME);

        parser = new WpasXmlParser();
        translator = new WpasHl7Translator(dateTimeProvider);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdtA28() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        var wpasMessage = parser.parse(TestUtil.getTestFileStream(WPAS_MPI_XML_PATH));
        var expected = TestUtil.getTestFileContent(EXPECTED_A28_PATH);

        // Act
        var result = translator.translate(wpasMessage);

        // Assert
        TestUtil.assertMatchingExpectedMessage(expected, result);
    }
}