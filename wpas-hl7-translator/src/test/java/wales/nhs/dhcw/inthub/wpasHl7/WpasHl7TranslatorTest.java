package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.Parser;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WpasHl7TranslatorTest {

    public static final String VALID_XML_PATH = "wpas-mpi.xml";

    WpasHl7Translator translator;

    @BeforeEach
    void setUp() throws JAXBException {
        var parser = new WpasXmlParser();
        translator = new WpasHl7Translator(parser);
    }

    @Test
    void WpasMpiMessage_Is_translatedToHl7() throws HL7Exception, JAXBException {

        var result = translator.translate(readTestFile(VALID_XML_PATH));

        assertNotNull(result);

        String encodedMessage = encodeHl7PipeAndHat(result);
        assertEquals("MSH|^~\\&|system456", encodedMessage.trim());
    }

    private static String encodeHl7PipeAndHat(AbstractMessage result) throws HL7Exception {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getPipeParser();
        String encodedMessage = parser.encode(result);
        return encodedMessage;
    }

    private Reader readTestFile(String path) {
        return new InputStreamReader(WpasXmlParserTest.class.getClassLoader().getResourceAsStream(path));
    }

}