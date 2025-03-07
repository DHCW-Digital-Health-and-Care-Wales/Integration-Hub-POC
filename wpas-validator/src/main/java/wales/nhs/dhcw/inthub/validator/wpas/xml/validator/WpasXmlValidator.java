package wales.nhs.dhcw.inthub.validator.wpas.xml.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.Optional;

public class WpasXmlValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WpasXmlValidator.class);
    private static final String SCHEMA_FILE = "WPAS_Schema.xsd";
    private final Validator validator;

    public WpasXmlValidator() {
        validator = getValidator();
    }

    private Validator getValidator() {
        final Validator validator;
        // Load XSD file
        InputStream xsdFile = WpasXmlValidator.class.getClassLoader().getResourceAsStream(SCHEMA_FILE);
        if (xsdFile == null) {
            throw new IllegalArgumentException("XSD file not found!");
        }

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = factory.newSchema(new StreamSource(xsdFile));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        validator = schema.newValidator();
        return validator;
    }

    public ValidationResult isValid(InputStream inputStream) {
        try {
            validator.validate(new StreamSource(inputStream));
            return new ValidationResult(true, Optional.empty()); // Validation successful
        } catch (Exception e) {
            LOGGER.error("XML Validation Error: {}", e.getMessage());
            return new ValidationResult(false, Optional.of(e.getMessage())); // Validation failed
        }
    }
}
