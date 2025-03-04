package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v231.message.ADT_A40;
import ca.uhn.hl7v2.model.v231.segment.MSH;
import jakarta.xml.bind.JAXBException;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.io.Reader;

public class WpasHl7Translator {
    WpasXmlParser xmlParser;

    public WpasHl7Translator(WpasXmlParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    AbstractMessage translate(Reader xmlMessage) throws JAXBException, DataTypeException {
        var message = xmlParser.parse(xmlMessage);
        switch (message.getTRANSACTION().getMSGID()) {
            case "MPI":
                return translateMpiToAdt40(message);
            default:
                throw new RuntimeException("Unknown message type: " + message.getTRANSACTION().getMSGID());
        }
    }

    private AbstractMessage translateMpiToAdt40(MAINDATA message) throws DataTypeException {
        var ad40 = new ADT_A40();
        buildMsh(ad40.getMSH(), message.getTRANSACTION());
        // TODO implement message mapping

        return ad40;
    }

    private void buildMsh(MSH msh, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        msh.getFieldSeparator().setValue("|");
        msh.getEncodingCharacters().setValue("^~\\&");
        msh.getSendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
        msh.getSendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
    }
}
