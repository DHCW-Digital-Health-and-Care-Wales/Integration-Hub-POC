package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WpasHl7Translator {
    private static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String HL7_VERSION = "2.5.1";

    private DateTimeFormatter dateTimeFormatter;

    public WpasHl7Translator() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    }

    AbstractMessage translate(MAINDATA wpasXml) throws DataTypeException {
        switch (wpasXml.getTRANSACTION().getMSGID()) {
            case "MPI":
                return translateMpiToAdtA28(wpasXml.getTRANSACTION());
            default:
                throw new RuntimeException("Unknown message type: " + wpasXml.getTRANSACTION().getMSGID());
        }
    }

    private AbstractMessage translateMpiToAdtA28(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var a28 = new ADT_A05();
        buildMsh(a28.getMSH(), transaction);
        // TODO implement message mapping

        return a28;
    }

    private void buildMsh(MSH msh, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        msh.getMsh1_FieldSeparator().setValue("|");
        msh.getMsh2_EncodingCharacters().setValue("^~\\&");
        msh.getMsh3_SendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
        msh.getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
        msh.getMsh7_DateTimeOfMessage().getTs1_Time().setValue(getCurrentDatetime());
        msh.getMsh9_MessageType().getMsg1_MessageCode().setValue("ADT");
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue("A28");
        msh.getMsh9_MessageType().getMsg3_MessageStructure().setValue("ADT_A05");
        msh.getMsh10_MessageControlID().setValue(transaction.getSYSTEMID() + transaction.getTRANSACTIONID());
        msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("P");
        msh.getMsh12_VersionID().getVersionID().setValue(HL7_VERSION);
        msh.getMsh15_AcceptAcknowledgmentType().setValue("AL");
    }

    private String getCurrentDatetime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
