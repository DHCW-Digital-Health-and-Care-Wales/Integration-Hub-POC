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
class MpaToAdtA31MapperTest {

    @Mock
    private DateTimeProvider dateTimeProvider;
    private WpasHl7Translator translator;
    private WpasXmlParser parser;

    @BeforeEach
    void setUp() throws JAXBException {
        parser = new WpasXmlParser();
        translator = new WpasHl7Translator(dateTimeProvider);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdtA28() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        String wpasMpiXmlPath = "wpas-mpa.xml";
        String msmqTime = "2025-02-10T14:52:16Z";
        String testTime = "20250210145216";
        String expectedA28Path = "wpas-adt_a31.hl7.xml";

        var wpasMessage = parser.parse(TestUtil.getTestFileStream(wpasMpiXmlPath));
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(testTime);
        Date msmqDateTime = Date.from(Instant.parse(msmqTime));
        var wpasData = new WpasData(wpasMessage, msmqDateTime);
        var expected = TestUtil.getTestFileContent(expectedA28Path);

        // Act
        var result = translator.translate(wpasData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdtA28WithNk1() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        String wpasMpiXmlPath = "wpas-mpa-with_nk1.xml";
        String msmqTime = "2025-03-19T12:16:05Z";
        String testTime = "20250319121606";
        String expectedA28Path = "wpas-adt_a31-with_nk1.hl7.xml";

        var wpasMessage = parser.parse(TestUtil.getTestFileStream(wpasMpiXmlPath));
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(testTime);
        Date msmqDateTime = Date.from(Instant.parse(msmqTime));
        var wpasData = new WpasData(wpasMessage, msmqDateTime);
        var expected = TestUtil.getTestFileContent(expectedA28Path);

        // Act
        var result = translator.translate(wpasData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }

}