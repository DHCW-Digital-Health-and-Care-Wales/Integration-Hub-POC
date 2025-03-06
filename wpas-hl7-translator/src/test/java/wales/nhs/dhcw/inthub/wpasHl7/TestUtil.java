package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

public final class TestUtil {
    private TestUtil() {
    }

    static Reader getTestFileReader(String path) {
        return new InputStreamReader(getTestFileStream(path));
    }

    static InputStream getTestFileStream(String path) {
        return WpasXmlParserTest.class.getClassLoader().getResourceAsStream(path);
    }

    static String getTestFileContent(String path) {
        return new BufferedReader(getTestFileReader(path))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    static void assertMatchingExpectedMessage(String expected, AbstractMessage actual)
            throws SAXException, HL7Exception {

        Assertions.assertEquals(
                EncodedMessageComparator.standardizeXML(expected),
                EncodedMessageComparator.standardizeXML(encodeHl7Xml(actual))
        );
    }

    static String encodeHl7Xml(AbstractMessage result) throws HL7Exception {
        HapiContext context = new DefaultHapiContext();
        Parser parser = context.getXMLParser();
        return parser.encode(result);
    }
}