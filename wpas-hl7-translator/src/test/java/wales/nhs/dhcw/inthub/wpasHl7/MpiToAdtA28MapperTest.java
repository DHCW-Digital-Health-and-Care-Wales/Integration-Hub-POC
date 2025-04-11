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
class MpiToAdtA28MapperTest {


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
        String wpasXmlMessagePath = "wpas-mpi.xml";
        String msmqTime = "2025-02-10T11:25:00Z";
        String testTime = "20250210112501";
        String expectedA28Path = "wpas-adt_a28.hl7.xml";

        when(dateTimeProvider.getCurrentDatetime()).thenReturn(testTime);
        var wpasMessage = parser.parse(TestUtil.getTestFileStream(wpasXmlMessagePath));
        Date dateTime = Date.from(Instant.parse(msmqTime));
        var wpasData = new WpasData(wpasMessage, dateTime);
        var expected = TestUtil.getTestFileContent(expectedA28Path);

        // Act
        var result = translator.translate(wpasData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }

    @Test
    void WpasMpiMessage_Is_translatedToAdtA28WithNk1() throws HL7Exception, JAXBException, SAXException {
        // Arrange
        String wpasXmlMessagePath = "wpas-mpi-A28-withNK1.xml";
        var testTime = "20250318172221";
        String msmqTime = "2025-03-18T17:22:20Z";
        String expectedHl7Path = "wpas-adt_a28-with_NK1.hl7.xml";

        var wpasMessage = parser.parse(TestUtil.getTestFileStream(wpasXmlMessagePath));
        when(dateTimeProvider.getCurrentDatetime()).thenReturn(testTime);
        Date msmqDateTime = Date.from(Instant.parse(msmqTime));
        var wpasData = new WpasData(wpasMessage, msmqDateTime);
        var expected = TestUtil.getTestFileContent(expectedHl7Path);


        // Act
        var result = translator.translate(wpasData);

        // Assert
        HL7Assertions.assertMatchingExpectedMessage(expected, result);
    }
}