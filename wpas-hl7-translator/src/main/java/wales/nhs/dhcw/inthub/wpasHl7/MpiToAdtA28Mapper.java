package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class MpiToAdtA28Mapper {


    private final DateTimeProvider dateTimeProvider;

    public MpiToAdtA28Mapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    AbstractMessage translateMpiToAdtA28(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var a28 = new ADT_A05();
        buildMsh(a28.getMSH(), transaction);
        // TODO implement message mapping

        return a28;
    }

    void buildMsh(MSH msh, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        msh.getMsh1_FieldSeparator().setValue("|");
        msh.getMsh2_EncodingCharacters().setValue("^~\\&");
        msh.getMsh3_SendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
        msh.getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
        msh.getMsh7_DateTimeOfMessage().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
        msh.getMsh9_MessageType().getMsg1_MessageCode().setValue("ADT");
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue("A28");
        msh.getMsh9_MessageType().getMsg3_MessageStructure().setValue("ADT_A05");
        msh.getMsh10_MessageControlID().setValue(transaction.getSYSTEMID() + transaction.getTRANSACTIONID());
        msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("P");
        msh.getMsh12_VersionID().getVersionID().setValue(WpasHl7Translator.HL7_VERSION);
        msh.getMsh15_AcceptAcknowledgmentType().setValue("AL");
    }
}