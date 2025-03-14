package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A39;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MprToAdtA40Mapper {
    private final ADT_A39 adtMessage;
    private final DateTimeProvider dateTimeProvider;

    public MprToAdtA40Mapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
        adtMessage = new ADT_A39();
    }

    public ADT_A39 ADT_A40Mapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        MshMapper(transaction);
        EVNMapper(transaction);
        PIDMapper(transaction);
        PD1Mapper(transaction);
        MRGMapper(transaction);
        return adtMessage;
    }


    public void MshMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {

        try {
            // Mapping header
            adtMessage.getMSH().getFieldSeparator().setValue(TransformerConstant.PIPE_LINE);
            adtMessage.getMSH().getEncodingCharacters().setValue(TransformerConstant.ENCODING_CHAR);
            adtMessage.getMSH().getMsh3_SendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
            adtMessage.getMSH().getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());

            adtMessage.getMSH().getDateTimeOfMessage().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());//yyyyMMddHHmmss
            adtMessage.getMSH().getMsh9_MessageType().getMsg1_MessageCode().setValue(TransformerConstant.ADT);
            adtMessage.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue(TransformerConstant.A40);
            adtMessage.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("ADT_A39");
            adtMessage.getMSH().getMsh10_MessageControlID().setValue(transaction.getSYSTEMID() + transaction.getTRANSACTIONID());
            adtMessage.getMSH().getProcessingID().getProcessingID().setValue("P");
            adtMessage.getMSH().getMsh12_VersionID().getVid1_VersionID().setValue("2.5.1");
            adtMessage.getMSH().getMsh15_AcceptAcknowledgmentType().setValue("AL");
        } catch (Exception e) {
            throw new DataTypeException("An Unexpected error while Mapping MSH element",e);
        }
    }

    public void EVNMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
//            adtMessage.getEVN().getEvn2_RecordedDateTime().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime()); // Need to do
            adtMessage.getEVN().getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(transaction.getUSERID());
//            adtMessage.getEVN().getEvn6_EventOccurred().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());// Need to do
            adtMessage.getEVN().getEvn7_EventFacility().getHd1_NamespaceID().setValue(" ");

        } catch (Exception e) {
            throw new DataTypeException("An Unexpected error while Mapping EVN element",e);
        }
    }

    public void PIDMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {

        try {
            adtMessage.getPATIENT().getPID().getPid1_SetIDPID().setValue("1");
            var patientIdentifierList1 = adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(0);
            patientIdentifierList1.getCx1_IDNumber().setValue(transaction.getNHSNUMBER());
            patientIdentifierList1.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("NHS");
            patientIdentifierList1.getCx5_IdentifierTypeCode().setValue("NH");

            var patientIdentifierList2 = adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(1);
            patientIdentifierList2.getCx1_IDNumber().setValue(transaction.getUNITNUMBER());
            patientIdentifierList2.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
            patientIdentifierList2.getCx5_IdentifierTypeCode().setValue("PI");

            var patientName1 =adtMessage.getPATIENT().getPID().getPid5_PatientName(0);
            patientName1.getXpn1_FamilyName().getFn1_Surname().setValue(transaction.getSURNAME());
            patientName1.getXpn2_GivenName().setValue(transaction.getFORENAME());
            patientName1.getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(" ");
            patientName1.getXpn5_PrefixEgDR().setValue(transaction.getTITLE());

            adtMessage.getPATIENT().getPID().getPid6_MotherSMaidenName(0).getXpn1_FamilyName().getFn1_Surname().setValue(" ");
            LocalDate date = LocalDate.parse(transaction.getBIRTHDATE(), dateTimeProvider.getDateFormatter());
            adtMessage.getPATIENT().getPID().getPid7_DateTimeOfBirth().getTs1_Time().setValue(date.format(dateTimeProvider.getDateyyyyMMddFormatter()));//yyyymmdd
            adtMessage.getPATIENT().getPID().getPid8_AdministrativeSex().setValue(transaction.getSEX());
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn1_FamilyName().getFn1_Surname().setValue(" ");
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn2_GivenName().setValue(" ");

            //11
            var patientAddress1 = adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0);
            patientAddress1.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(transaction.getADDRESS1());
            patientAddress1.getXad2_OtherDesignation().setValue(transaction.getADDRESS2());
            patientAddress1.getXad3_City().setValue(transaction.getADDRESS3());
            patientAddress1.getXad4_StateOrProvince().setValue(transaction.getADDRESS4());
            patientAddress1.getXad5_ZipOrPostalCode().setValue(transaction.getPOSTCODE());
            patientAddress1.getXad7_AddressType().setValue("H");
            var patientAddress2 = adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1);
            patientAddress2.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(transaction.getCONTACTADDRESS1());
            patientAddress2.getXad2_OtherDesignation().setValue(transaction.getCONTACTADDRESS2());
            patientAddress2.getXad3_City().setValue(transaction.getCONTACTADDRESS3());
            patientAddress2.getXad4_StateOrProvince().setValue(transaction.getCONTACTADDRESS4());
            patientAddress2.getXad5_ZipOrPostalCode().setValue(transaction.getCONTACTADDRESS6());
            patientAddress2.getXad7_AddressType().setValue("M");

            //13
            var phoneNumber0 = adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0);
            phoneNumber0.getXtn1_TelephoneNumber().setValue(transaction.getTELEPHONEDAY());
            phoneNumber0.getXtn2_TelecommunicationUseCode().setValue("PRN");
            phoneNumber0.getXtn3_TelecommunicationEquipmentType().setValue("PH");
            phoneNumber0.getXtn4_EmailAddress().setValue(transaction.getEMAIL());
            phoneNumber0.getXtn9_AnyText().setValue("DAY");
            var phoneNumber1 = adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1);
            phoneNumber1.getXtn1_TelephoneNumber().setValue(transaction.getTELEPHONENIGHT());
            phoneNumber1.getXtn2_TelecommunicationUseCode().setValue("PRN");
            phoneNumber1.getXtn3_TelecommunicationEquipmentType().setValue("PH");
            phoneNumber1.getXtn9_AnyText().setValue("NIGHT");
            var phoneNumber2 = adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2);
            phoneNumber2.getXtn1_TelephoneNumber().setValue(transaction.getMOBILE());
            phoneNumber2.getXtn2_TelecommunicationUseCode().setValue("PRS");
            phoneNumber2.getXtn3_TelecommunicationEquipmentType().setValue("CP");
            phoneNumber2.getXtn9_AnyText().setValue("Mobile");

            adtMessage.getPATIENT().getPID().getPid16_MaritalStatus().getCe1_Identifier().setValue(transaction.getMARITALSTATUS());
            adtMessage.getPATIENT().getPID().getPid17_Religion().getCe1_Identifier().setValue(transaction.getRELIGIONSTATUS());
            adtMessage.getPATIENT().getPID().getPid22_EthnicGroup(0).getCe1_Identifier().setValue(transaction.getETHNICORIGIN());
            if (transaction.getDEATHDATE() != null && !transaction.getDEATHDATE().isBlank()) {
                LocalDate deathDate = LocalDate.parse(transaction.getBIRTHDATE(), dateTimeProvider.getDateFormatter());
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(deathDate.format(dateTimeProvider.getDateyyyyMMddFormatter()));
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("Y");
            } else {
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(transaction.getDEATHDATE()); //yyyyMMdd
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("N");
            }
            adtMessage.getPATIENT().getPID().getPid32_IdentityReliabilityCode(0).setValue(transaction.getNHSCERTIFICATION());

            LocalDateTime dateTime = LocalDateTime.parse(transaction.getUPDATEDATE(), dateTimeProvider.getDateTimeFormatterWithTFormat());
            adtMessage.getPATIENT().getPID().getPid33_LastUpdateDateTime().getTs1_Time().setValue(dateTime.format(dateTimeProvider.getDateTimeFormatter()));

        } catch (Exception e) {
            throw new DataTypeException("An Unexpected error while Mapping MRG element",e);
        }
    }

    public void PD1Mapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
            adtMessage.getPATIENT().getPD1().getPd13_PatientPrimaryFacility(0).getXon1_OrganizationName().setValue(transaction.getGPADDR1());
            adtMessage.getPATIENT().getPD1().getPd13_PatientPrimaryFacility(0).getXon10_OrganizationIdentifier().setValue(transaction.getGPPRACTICE());
            adtMessage.getPATIENT().getPD1().getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn1_IDNumber().setValue(transaction.getREGISTEREDGP());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getGPSURNAME());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn3_GivenName().setValue(transaction.getGPINITS());

        } catch (Exception e) {
            throw new DataTypeException("An Unexpected error while Mapping P element",e);
        }
    }

    public void MRGMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
            var priorPatientIdentifierList = adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIdentifierList(0);
            priorPatientIdentifierList.getCx1_IDNumber().setValue(transaction.getOLDUNITNUMBER());
            priorPatientIdentifierList.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
            priorPatientIdentifierList.getCx5_IdentifierTypeCode().setValue("PI");
        } catch (Exception e) {
            throw new DataTypeException("An Unexpected error while Mapping MRG element",e);
        }
    }
}

