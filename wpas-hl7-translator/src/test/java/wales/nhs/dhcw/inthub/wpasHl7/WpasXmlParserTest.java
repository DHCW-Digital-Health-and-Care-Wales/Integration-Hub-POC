package wales.nhs.dhcw.inthub.wpasHl7;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class WpasXmlParserTest {

    public static final String VALID_XML_PATH = "wpas-mpi.xml";

    @Test
    void ParserReturnsParsedMessage() throws JAXBException, IOException {
        // Arrange
        var parser = new WpasXmlParser();
        var xml = readTestFile(VALID_XML_PATH);

        // Act
        var message = parser.parse(xml);

        // Assert
        assertNotNull(message);
        assertEquals("12345", message.getTRANSACTION().getTRANSACTIONID());
    }

    private Reader readTestFile(String path) {
        return new InputStreamReader(WpasXmlParserTest.class.getClassLoader().getResourceAsStream(path));
    }
}