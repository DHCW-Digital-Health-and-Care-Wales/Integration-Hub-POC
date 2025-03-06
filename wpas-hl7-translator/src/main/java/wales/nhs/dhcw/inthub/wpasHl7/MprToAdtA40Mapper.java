package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A39;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MprToAdtA40Mapper {
    private ADT_A39 adtMessage;
    private final DateTimeProvider dateTimeProvider;

    public MprToAdtA40Mapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
        adtMessage = new ADT_A39();
    }

    public ADT_A39 ADT_A40Mapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        if (MshMapper(transaction) != null) {
            if (EVNMapper(transaction) != null && PIDMapper(transaction) != null && PD1Mapper(transaction) != null) {
                MRGMapper(transaction);
            }
        } else {
            return null;
        }
        return adtMessage;
    }


    public ADT_A39 MshMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {

        try {
            // Mapping header
            adtMessage.getMSH().getFieldSeparator().setValue("|");
            adtMessage.getMSH().getEncodingCharacters().setValue("^~\\&");
            adtMessage.getMSH().getMsh3_SendingApplication().getNamespaceID().setValue(transaction.getSYSTEMID());
            adtMessage.getMSH().getMsh4_SendingFacility().getNamespaceID().setValue(transaction.getDHACODE());
            adtMessage.getMSH().getReceivingApplication().getNamespaceID().setValue("200");
            adtMessage.getMSH().getReceivingFacility().getNamespaceID().setValue("200");

            adtMessage.getMSH().getDateTimeOfMessage().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());//yyyyMMddHHmmss
            adtMessage.getMSH().getMsh9_MessageType().getMsg1_MessageCode().setValue("ADT");
            adtMessage.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue("A40");
            adtMessage.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("ADT_A39");
            adtMessage.getMSH().getMsh10_MessageControlID().setValue(transaction.getSYSTEMID() + transaction.getTRANSACTIONID());
            adtMessage.getMSH().getProcessingID().getProcessingID().setValue("P");
            adtMessage.getMSH().getMsh12_VersionID().getVid1_VersionID().setValue("2.5.1");
            adtMessage.getMSH().getMsh15_AcceptAcknowledgmentType().setValue("AL");
            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 EVNMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
            adtMessage.getEVN().getEvn2_RecordedDateTime().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
            adtMessage.getEVN().getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(transaction.getUSERID());
            adtMessage.getEVN().getEvn6_EventOccurred().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
            adtMessage.getEVN().getEvn7_EventFacility().getHd1_NamespaceID().setValue(" ");

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 PIDMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        try {
            adtMessage.getPATIENT().getPID().getPid1_SetIDPID().setValue("1");
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getCx1_IDNumber().setValue(transaction.getNHSNUMBER());
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue("NHS");
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(0).getCx5_IdentifierTypeCode().setValue("NH");
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(1).getCx1_IDNumber().setValue(transaction.getUNITNUMBER());
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(1).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
            adtMessage.getPATIENT().getPID().getPid3_PatientIdentifierList(1).getCx5_IdentifierTypeCode().setValue("PI");

            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().setValue(transaction.getSURNAME());
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn2_GivenName().setValue(transaction.getFORENAME());
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(" ");
            adtMessage.getPATIENT().getPID().getPid5_PatientName(0).getXpn5_PrefixEgDR().setValue(transaction.getTITLE());

            adtMessage.getPATIENT().getPID().getPid6_MotherSMaidenName(0).getXpn1_FamilyName().getFn1_Surname().setValue(" ");
            LocalDate date = LocalDate.parse(transaction.getBIRTHDATE(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String outputDate = date.format(outputFormatter);
            adtMessage.getPATIENT().getPID().getPid7_DateTimeOfBirth().getTs1_Time().setValue(outputDate);//yyyymmdd
            adtMessage.getPATIENT().getPID().getPid8_AdministrativeSex().setValue(transaction.getSEX());
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn1_FamilyName().getFn1_Surname().setValue(" ");
            adtMessage.getPATIENT().getPID().getPid9_PatientAlias(0).getXpn2_GivenName().setValue(" ");

            //11
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(transaction.getADDRESS1());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad2_OtherDesignation().setValue(transaction.getADDRESS2());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad3_City().setValue(transaction.getADDRESS3());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad4_StateOrProvince().setValue(transaction.getADDRESS4());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad5_ZipOrPostalCode().setValue(transaction.getPOSTCODE());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(0).getXad7_AddressType().setValue("H");
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(transaction.getCONTACTADDRESS1());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad2_OtherDesignation().setValue(transaction.getCONTACTADDRESS2());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad3_City().setValue(transaction.getCONTACTADDRESS3());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad4_StateOrProvince().setValue(transaction.getCONTACTADDRESS4());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad5_ZipOrPostalCode().setValue(transaction.getCONTACTADDRESS6());
            adtMessage.getPATIENT().getPID().getPid11_PatientAddress(1).getXad7_AddressType().setValue("M");

            //13
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn1_TelephoneNumber().setValue(transaction.getTELEPHONEDAY());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn2_TelecommunicationUseCode().setValue("PRN");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn3_TelecommunicationEquipmentType().setValue("PH");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn4_EmailAddress().setValue(transaction.getEMAIL());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(0).getXtn9_AnyText().setValue("DAY");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn1_TelephoneNumber().setValue(transaction.getTELEPHONENIGHT());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn2_TelecommunicationUseCode().setValue("PRN");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn3_TelecommunicationEquipmentType().setValue("PH");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(1).getXtn9_AnyText().setValue("NIGHT");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn1_TelephoneNumber().setValue(transaction.getMOBILE());
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn2_TelecommunicationUseCode().setValue("PRS");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn3_TelecommunicationEquipmentType().setValue("CP");
            adtMessage.getPATIENT().getPID().getPid13_PhoneNumberHome(2).getXtn9_AnyText().setValue("Mobile");

            adtMessage.getPATIENT().getPID().getPid16_MaritalStatus().getCe1_Identifier().setValue(transaction.getMARITALSTATUS());
            adtMessage.getPATIENT().getPID().getPid17_Religion().getCe1_Identifier().setValue(transaction.getRELIGIONSTATUS());
            adtMessage.getPATIENT().getPID().getPid22_EthnicGroup(0).getCe1_Identifier().setValue(transaction.getETHNICORIGIN());
            if (transaction.getDEATHDATE() != null && !transaction.getDEATHDATE().isBlank()) {
                LocalDate date1 = LocalDate.parse(transaction.getBIRTHDATE(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String deathDate = date1.format(outputFormatter);
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(deathDate);
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("Y");
            } else {
                adtMessage.getPATIENT().getPID().getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(transaction.getDEATHDATE()); //yyyyMMdd
                adtMessage.getPATIENT().getPID().getPid30_PatientDeathIndicator().setValue("N");
            }
            adtMessage.getPATIENT().getPID().getPid32_IdentityReliabilityCode(0).setValue(transaction.getNHSCERTIFICATION());

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(transaction.getUPDATEDATE(), inputFormatter);
            adtMessage.getPATIENT().getPID().getPid33_LastUpdateDateTime().getTs1_Time().setValue(dateTime.format(dateTimeFormatter));

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 PD1Mapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
            adtMessage.getPATIENT().getPD1().getPd13_PatientPrimaryFacility(0).getXon1_OrganizationName().setValue(transaction.getGPADDR1());
            adtMessage.getPATIENT().getPD1().getPd13_PatientPrimaryFacility(0).getXon10_OrganizationIdentifier().setValue(transaction.getGPPRACTICE());
            adtMessage.getPATIENT().getPD1().getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn1_IDNumber().setValue(transaction.getREGISTEREDGP());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getGPSURNAME());
            adtMessage.getPATIENT().getPD1().getPatientPrimaryCareProviderNameIDNo(0).getXcn3_GivenName().setValue(transaction.getGPINITS());

            return adtMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ADT_A39 MRGMapper(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        try {
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIdentifierList(0).getCx1_IDNumber().setValue(transaction.getOLDUNITNUMBER());
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIdentifierList(0).getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
            adtMessage.getPATIENT().getMRG().getMrg1_PriorPatientIdentifierList(0).getCx5_IdentifierTypeCode().setValue("PI");
            return adtMessage;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

