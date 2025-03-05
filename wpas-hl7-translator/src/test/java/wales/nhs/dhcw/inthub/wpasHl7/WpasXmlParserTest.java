package wales.nhs.dhcw.inthub.wpasHl7;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class WpasXmlParserTest {

    public static final String WPAS_UTF8_PATH = "wpas-simple-mpi.xml";
    public static final String WPAS_UTF16_PATH = "wpas-mpi.xml";

    @Test
    void WpasUtf8XmlIsParsed() throws JAXBException, IOException {
        // Arrange
        var parser = new WpasXmlParser();
        var xml = readTestFile(WPAS_UTF8_PATH);

        // Act
        var message = parser.parse(xml);

        // Assert
        assertNotNull(message);
        assertEquals("12345", message.getTRANSACTION().getTRANSACTIONID());
    }

    @Test
    void WpasUtf16XmlIsParsed() throws JAXBException, IOException {
        // Arrange
        var parser = new WpasXmlParser();
        var xml = readTestFile(WPAS_UTF16_PATH);

        // Act
        var message = parser.parse(xml);

        // Assert
        assertNotNull(message);
        assertEquals("28038161", message.getTRANSACTION().getTRANSACTIONID());
    }

    private InputStream readTestFile(String path) {
        return WpasXmlParserTest.class.getClassLoader().getResourceAsStream(path);
    }
}