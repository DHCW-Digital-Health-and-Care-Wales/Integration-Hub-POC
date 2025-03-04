package wales.nhs.dhcw.inthub.wpasHl7;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.io.InputStream;

public class WpasXmlParser {

    private JAXBContext context;

    public WpasXmlParser() throws JAXBException {
        context = JAXBContext.newInstance( MAINDATA.class );
    }

    public MAINDATA parse(InputStream xml) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        return (MAINDATA) jaxbUnmarshaller.unmarshal(xml);
    }
}
