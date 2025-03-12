package wales.nhs.dhcw.inthub.validator;

import org.junit.jupiter.api.Test;
import wales.nhs.dhcw.msgbus.ProcessingResult;
import wales.nhs.dhcw.inthub.validator.wpas.xml.validator.WpasXmlValidator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WpasXmlValidatorTest {

    WpasXmlValidator wpasXmlValidator = new WpasXmlValidator();

    @Test
    void isValid_called_with_valid_utf_8_xml_returns_true() throws IOException {
        ProcessingResult result;

        try (InputStream inputStream = WpasXmlValidatorTest.class.getResourceAsStream("/wpas-mpa.xml");) {
            result = wpasXmlValidator.isValid(inputStream);
        }

        assertTrue(result.success());
    }

    @Test
    void isValid_called_with_valid_utf_16_xml_returns_true() throws IOException {
        ProcessingResult result;

        try (InputStream inputStream = WpasXmlValidatorTest.class.getResourceAsStream("/wpas-mpi.xml");) {
            result = wpasXmlValidator.isValid(inputStream);
        }

        assertTrue(result.success());
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
        ProcessingResult result = wpasXmlValidator.isValid(inputStream);

        assertFalse(result.success());
    }

    @Test
    void isValid_called_with_empty_xml_returns_false() {
        String emptyXml = "";

        InputStream inputStream = new ByteArrayInputStream(emptyXml.getBytes());
        ProcessingResult result = wpasXmlValidator.isValid(inputStream);

        assertFalse(result.success());
    }
}

