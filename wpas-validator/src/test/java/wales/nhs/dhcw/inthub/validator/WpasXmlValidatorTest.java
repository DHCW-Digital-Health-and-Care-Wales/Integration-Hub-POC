package wales.nhs.dhcw.inthub.validator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

class WpasXmlValidatorTest {

    WpasXmlValidator wpasXmlValidator = new WpasXmlValidator();

    @Test
    void isValid_called_with_valid_utf_8_xml_returns_true() throws IOException {
        boolean result;

        try (InputStream inputStream = WpasXmlValidatorTest.class.getResourceAsStream("/wpas-mpa.xml");) {
            result = wpasXmlValidator.isValid(inputStream);
        }

        assertTrue(result);
    }

    @Test
    void isValid_called_with_valid_utf_16_xml_returns_true() throws IOException {
        boolean result;

        try (InputStream inputStream = WpasXmlValidatorTest.class.getResourceAsStream("/wpas-mpi.xml");) {
            result = wpasXmlValidator.isValid(inputStream);
        }

        assertTrue(result);
    }

    @Test
    void isValid_called_with_invalid_xml_returns_false() {
        String invalidXml = """
        <?xml version="1.0" encoding="UTF-16"?>
        <MAINDATA xmlns="http://PAS_Demographics">
            <TRANSACTION>
                <TRANSACTION_ID>12345</TRANSACTION_ID>
            </TRANSACTION>
        </MAINDATA>
        """;

        InputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes(StandardCharsets.UTF_16));
        boolean result = wpasXmlValidator.isValid(inputStream);

        assertFalse(result);
    }

    @Test
    void isValid_called_with_empty_xml_returns_false() {
        String emptyXml = "";

        InputStream inputStream = new ByteArrayInputStream(emptyXml.getBytes());
        boolean result = wpasXmlValidator.isValid(inputStream);

        assertFalse(result);
    }
}

