package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.datatype.XTN;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.segment.*;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MpiToAdtA28Mapper {


    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String FIXED_PID_ID = "1";
    public static final String NHS_IDENTIFIER_AUTHORITY = "NHS";
    public static final String NHS_IDENTIFIER_TYPE_CODE = "NH";
    public static final String PATIENT_IDENTIFIER_TYPE_CODE = "PI";
    public static final String ADDRESS_TYPE_HOME = "H";
    public static final String ADDRESS_TYPE_MAILING = "M";
    public static final String TELECOM_USE_PRIMARY_RESIDENCE_NUMBER = "PRN";
    public static final String TELECOM_EQUPIMENT_CODE_PHONE = "PH";
    public static final String TELECOM_DAY = "DAY";
    private static final String TELECOM_NIGHT = "Night";
    public static final String TELECOM_USE_PERSONAL = "PRS";
    public static final String TELECOM_EQUPIMENT_CODE_MOBILE = "CP";
    public static final String TELECOM_MOBILE = "Mobile";
    private final DateTimeProvider dateTimeProvider;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dateTimeFormatter;


    public MpiToAdtA28Mapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeProvider.DATETIME_FORMAT);
    }

    AbstractMessage translateMpiToAdtA28(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var a28 = new ADT_A05();
        buildMsh(a28.getMSH(), transaction);
        buildEvn(a28.getEVN(), transaction);
        buildPid(a28.getPID(), transaction);
        buildPD1(a28.getPD1(), transaction);
        build1stNK1(a28.getNK1(0), transaction);
        build2ndNK1(a28.getNK1(1), transaction);
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

    private void buildEvn(EVN evn, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        evn.getEvn2_RecordedDateTime().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
        evn.getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(transaction.getUSERID());
        // TODO - get MSMQ timestamp from metadata and set for the EVN-6.1
        evn.getEvn6_EventOccurred().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
    }

    private void buildPid(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pid.getPid1_SetIDPID().setValue(FIXED_PID_ID);
        var patientIdentifier1 = pid.getPid3_PatientIdentifierList(0);
        if (notNullNorBlank(transaction.getNHSNUMBER())) {
            var nhsNumber = stripEmptyDoubleQuotes(transaction.getNHSNUMBER());
            patientIdentifier1.getCx1_IDNumber().setValue(nhsNumber);
            patientIdentifier1.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(NHS_IDENTIFIER_AUTHORITY);
            patientIdentifier1.getCx5_IdentifierTypeCode().setValue(NHS_IDENTIFIER_TYPE_CODE);
        }

        var patientIdentifier2 = pid.getPid3_PatientIdentifierList(1);
        patientIdentifier2.getCx1_IDNumber().setValue(stripEmptyDoubleQuotes(transaction.getUNITNUMBER()));
        if (notNullNorBlank(transaction.getUNITNUMBER())) {
            patientIdentifier2.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(
                    stripEmptyDoubleQuotes(transaction.getSYSTEMID())
            );
            patientIdentifier2.getCx5_IdentifierTypeCode().setValue(PATIENT_IDENTIFIER_TYPE_CODE);
        }

        var patientName = pid.getPid5_PatientName(0);
        patientName.getXpn1_FamilyName().getFn1_Surname().setValue(stripEmptyDoubleQuotes(transaction.getSURNAME()));
        patientName.getXpn2_GivenName().setValue(readPatientGivenName(transaction.getFORENAME()));
        patientName.getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(readPatientMiddleNames(transaction.getFORENAME()));

        patientName.getXpn5_PrefixEgDR().setValue(stripEmptyDoubleQuotes(transaction.getTITLE()));

        pid.getPid6_MotherSMaidenName(0).getXpn1_FamilyName().getFn1_Surname().setValue(stripEmptyDoubleQuotes(transaction.getMAIDENNAME()));
        pid.getPid7_DateTimeOfBirth().getTs1_Time().setValue(mapDateFormat(transaction.getBIRTHDATE()));
        pid.getPid8_AdministrativeSex().setValue(stripEmptyDoubleQuotes(transaction.getSEX()));
        pid.getPid9_PatientAlias(0).getXpn1_FamilyName().getFn1_Surname().setValue(stripEmptyDoubleQuotes(transaction.getALIASSURNAME()));
        pid.getPid9_PatientAlias(0).getXpn2_GivenName().setValue(stripEmptyDoubleQuotes(transaction.getALIASFORENAME()));
        XAD patientAddress = pid.getPid11_PatientAddress(0);
        patientAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(stripEmptyDoubleQuotes(transaction.getADDRESS1()));
        patientAddress.getXad2_OtherDesignation().setValue(stripEmptyDoubleQuotes(transaction.getADDRESS2()));
        patientAddress.getXad3_City().setValue(stripEmptyDoubleQuotes(transaction.getADDRESS3()));
        if (notNullNorBlank(transaction.getADDRESS4()) && notNullNorBlank(transaction.getADDRESS5())) {
            patientAddress.getXad4_StateOrProvince().setValue(transaction.getADDRESS4() + "," + transaction.getADDRESS5());
        }
        patientAddress.getXad5_ZipOrPostalCode().setValue(stripEmptyDoubleQuotes(transaction.getPOSTCODE()));
        patientAddress.getXad7_AddressType().setValue(ADDRESS_TYPE_HOME);

        var patientAddress2 = pid.getPid11_PatientAddress(1);
        patientAddress2.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(
                stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS1())
        );
        patientAddress2.getXad2_OtherDesignation().setValue(stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS2()));
        patientAddress2.getXad3_City().setValue(stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS3()));
        if (notNullNorBlank(transaction.getCONTACTADDRESS4()) && notNullNorBlank(transaction.getCONTACTADDRESS5())) {
            patientAddress2.getXad4_StateOrProvince().setValue(transaction.getCONTACTADDRESS4() + "," + transaction.getCONTACTADDRESS5());
        }
        patientAddress2.getXad5_ZipOrPostalCode().setValue(stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS6()));
        patientAddress2.getXad7_AddressType().setValue(ADDRESS_TYPE_MAILING);

        var phoneNumber1 = pid.getPid13_PhoneNumberHome(0);
        phoneNumber1.getXtn1_TelephoneNumber().setValue(stripEmptyDoubleQuotes(transaction.getTELEPHONEDAY()));
        phoneNumber1.getXtn4_EmailAddress().setValue(stripEmptyDoubleQuotes(transaction.getEMAIL()));
        if (this.notNullNorBlank(transaction.getTELEPHONEDAY())) {
            phoneNumber1.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber1.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber1.getXtn9_AnyText().setValue(TELECOM_DAY);
        }

        var phoneNumber2 = pid.getPid13_PhoneNumberHome(1);
        phoneNumber2.getXtn1_TelephoneNumber().setValue(stripEmptyDoubleQuotes(transaction.getTELEPHONENIGHT()));
        if (this.notNullNorBlank(transaction.getTELEPHONENIGHT())) {
            phoneNumber2.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber2.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber2.getXtn9_AnyText().setValue(TELECOM_NIGHT);
        }
        var phoneNumber3 = pid.getPid13_PhoneNumberHome(1);
        phoneNumber3.getXtn1_TelephoneNumber().setValue(stripEmptyDoubleQuotes(transaction.getMOBILE()));
        if (this.notNullNorBlank(transaction.getMOBILE())) {
            phoneNumber3.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PERSONAL);
            phoneNumber3.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_MOBILE);
            phoneNumber3.getXtn9_AnyText().setValue(TELECOM_MOBILE);
        }

        pid.getPid16_MaritalStatus().getCe1_Identifier().setValue(transaction.getMARITALSTATUS());
        pid.getPid17_Religion().getCe1_Identifier().setValue(transaction.getRELIGIONSTATUS());
        pid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue(transaction.getETHNICORIGIN());
        pid.getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(mapDateFormat(transaction.getDEATHDATE()));
        pid.getPid30_PatientDeathIndicator().setValue(
                notNullNorBlank(transaction.getDEATHDATE()) ? "Y" : "N"
        );
        pid.getPid32_IdentityReliabilityCode(0).setValue(stripEmptyDoubleQuotes(transaction.getNHSCERTIFICATION()));
        pid.getPid33_LastUpdateDateTime().getTs1_Time().setValue(mapDateTimeFormat(transaction.getUPDATEDATE()));
    }

    private void buildPD1(PD1 pd1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pd1.getPd13_PatientPrimaryFacility(0).getXon1_OrganizationName().setValue(transaction.getGPADDR1());
        pd1.getPd13_PatientPrimaryFacility(0).getXon10_OrganizationIdentifier().setValue(transaction.getGPPRACTICE());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn1_IDNumber().setValue(transaction.getREGISTEREDGP());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getGPSURNAME());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn3_GivenName().setValue(transaction.getGPINITS());
    }

    private void build1stNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        nk1.getNk11_SetIDNK1().setValue("1");
        nk1.getNk12_Name(0).getXpn1_FamilyName().getFn1_Surname().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKIN()));
        nk1.getNk13_Relationship().getCe1_Identifier().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINRELCODE()));
        nk1.getNk13_Relationship().getCe2_Text().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINREL()));

        XAD nkAddress = nk1.getNk14_Address(0);
        nkAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINADDR1()));
        nkAddress.getXad2_OtherDesignation().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINADDR2()));
        nkAddress.getXad3_City().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINADDR3()));
        nkAddress.getXad4_StateOrProvince().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINADDR4()));
        nkAddress.getXad5_ZipOrPostalCode().setValue(stripEmptyDoubleQuotes(transaction.getNEXTKINPOSTCODE()));

        XTN phoneNumber = nk1.getNk15_PhoneNumber(0);
        if (notNullNorBlank(transaction.getNEXTKINTELDAY())) {
            phoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getNEXTKINTELDAY());
            phoneNumber.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber.getXtn9_AnyText().setValue(TELECOM_DAY);
        }

        var nightPhoneNumber = nk1.getNk15_PhoneNumber(1);
        if (notNullNorBlank(transaction.getNEXTKINTELNIGHT())) {
            nightPhoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getNEXTKINTELNIGHT());
            nightPhoneNumber.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            nightPhoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            nightPhoneNumber.getXtn9_AnyText().setValue(TELECOM_NIGHT);
        }
    }

    private void build2ndNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        nk1.getNk11_SetIDNK1().setValue("2");
        nk1.getNk12_Name(0).getXpn1_FamilyName().getFn1_Surname().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKIN()));
        nk1.getNk13_Relationship().getCe1_Identifier().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINRELCODE()));
        nk1.getNk13_Relationship().getCe2_Text().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINREL()));

        XAD nkAddress = nk1.getNk14_Address(0);
        nkAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINADDR1()));
        nkAddress.getXad2_OtherDesignation().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINADDR2()));
        nkAddress.getXad3_City().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINADDR3()));
        nkAddress.getXad4_StateOrProvince().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINADDR4()));
        nkAddress.getXad5_ZipOrPostalCode().setValue(stripEmptyDoubleQuotes(transaction.getOTHERKINPOSTCODE()));

        XTN phoneNumber = nk1.getNk15_PhoneNumber(0);
        if (notNullNorBlank(transaction.getOTHERKINTELDAY())) {
            phoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getOTHERKINTELDAY());
            phoneNumber.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber.getXtn9_AnyText().setValue(TELECOM_DAY);
        }

        var nightPhoneNumber = nk1.getNk15_PhoneNumber(1);
        if (notNullNorBlank(transaction.getOTHERKINTELNIGHT())) {
            nightPhoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getOTHERKINTELNIGHT());
            nightPhoneNumber.getXtn2_TelecommunicationUseCode().setValue(TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            nightPhoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(TELECOM_EQUPIMENT_CODE_PHONE);
            nightPhoneNumber.getXtn9_AnyText().setValue(TELECOM_NIGHT);
        }

    }



    private String mapDateFormat(String dateString) {
        var cleanedDateString = stripEmptyDoubleQuotes(dateString);
        if (notNullNorBlank(cleanedDateString)) {
            LocalDate date = LocalDate.parse(cleanedDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return date.format(dateFormatter);
        }
        else {
            return dateString;
        }
    }

    private String mapDateTimeFormat(String dateTimeString) {
        if (notNullNorBlank(dateTimeString)) {
            var date = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            return date.format(dateTimeFormatter);
        }
        else {
            return dateTimeString;
        }
    }

    private String readPatientGivenName(String forename) {
        if (this.notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(0, spacePosition);
            } else {
                return forename;
            }
        }
        return "";
    }

    private String readPatientMiddleNames(String forename) {
        if (this.notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(spacePosition);
            }
        }
        return "";
    }

    private boolean notNullNorBlank(String string) {
        return null != string && !stripEmptyDoubleQuotes(string).isBlank();
    }

    private static String stripEmptyDoubleQuotes(String input) {
        if (input != null && input.equals("\"\"")) {
            return "";
        } else {
            return input;
        }
    }

}
