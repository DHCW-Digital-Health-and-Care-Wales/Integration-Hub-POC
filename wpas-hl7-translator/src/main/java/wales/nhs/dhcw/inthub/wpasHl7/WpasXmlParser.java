package wales.nhs.dhcw.inthub.wpasHl7;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;

public class WpasXmlParser {

    private JAXBContext context;

    public WpasXmlParser() throws JAXBException {
        context = JAXBContext.newInstance( MAINDATA.class );
    }

    public MAINDATA parse(InputStream xmlStream) throws JAXBException {
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            return (MAINDATA) jaxbUnmarshaller.unmarshal(xmlStream);
    }

    public MAINDATA parseUTF8(InputStream xmlStream) throws JAXBException {
            Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
            InputStreamReader reader = new InputStreamReader(xmlStream, StandardCharsets.UTF_8);
            return (MAINDATA) jaxbUnmarshaller.unmarshal(reader);
    }
}
