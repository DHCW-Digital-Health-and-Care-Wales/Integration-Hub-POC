package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.WpasHl7Translator;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.Hl7MessageTypeData;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class MshMapper {
    private final DateTimeProvider dateTimeProvider;

    public MshMapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    public void buildMsh(MSH msh, MAINDATA.TRANSACTION transaction, Hl7MessageTypeData hl7MessageTypeData) throws DataTypeException {
        msh.getFieldSeparator().setValue(HL7Constants.PIPE_LINE);
        msh.getEncodingCharacters().setValue(HL7Constants.ENCODING_CHAR);
        msh.getMsh3_SendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
//        msh.getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
        setMsh4_SendingFacility(msh, transaction);
        msh.getMsh7_DateTimeOfMessage().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
        msh.getMsh9_MessageType().getMsg1_MessageCode().setValue(hl7MessageTypeData.messageTypeCode());
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue(hl7MessageTypeData.triggerEvent());
        msh.getMsh9_MessageType().getMsg3_MessageStructure().setValue(hl7MessageTypeData.messageStructure());
        msh.getMsh10_MessageControlID().setValue(transaction.getSYSTEMID() + transaction.getTRANSACTIONID());
        msh.getMsh11_ProcessingID().getPt1_ProcessingID().setValue("P");
        msh.getMsh12_VersionID().getVersionID().setValue(WpasHl7Translator.HL7_VERSION);
        msh.getMsh15_AcceptAcknowledgmentType().setValue("AL");
    }

    private void setMsh4_SendingFacility(MSH msh, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        if (transaction.getMSGID().equals("IPI")) {
            msh.getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getCURLOCPROVIDERCODE());
        }else{
            msh.getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
        }
    }
}
