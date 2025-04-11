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
class IptToAdtA02MapperTest {

    private static final String WPAS_IPT_XML_PATH = "wpas-ipt.xml";
    private static final String EXPECTED_A02_PATH = "wpas-adt_a02.hl7.xml";
    private static final String DUMMY_TEST_TIME = "20250403163027";
    private static final String TEST_TIME = "2025-03-26T12:22:27Z";

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
    void WpasIptMessage_Is_translatedToAdtA02() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        var wpasMessage = parser.parse(TestUtil.getTestFileStream(WPAS_IPT_XML_PATH));
        var wpasData = new WpasData(wpasMessage, null);
        var expected = TestUtil.getTestFileContent(EXPECTED_A02_PATH);

        // Act
        var result = translator.translate(wpasData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }
}
