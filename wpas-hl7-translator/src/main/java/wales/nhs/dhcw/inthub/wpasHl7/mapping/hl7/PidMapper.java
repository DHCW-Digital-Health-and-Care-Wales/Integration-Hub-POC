package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.segment.PID;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class PidMapper {

    public static final String FIXED_PID_ID = "1";
    public static final String NHS_IDENTIFIER_AUTHORITY = "NHS";
    public static final String NHS_IDENTIFIER_TYPE_CODE = "NH";
    public static final String PATIENT_IDENTIFIER_TYPE_CODE = "PI";


    private final MapUtils utils;

    public PidMapper() {
        utils = new MapUtils();
    }

    public void buildPid(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        buildIdentifiers(pid, transaction);
        buildPatientName(pid, transaction);
        buildGenericData(pid, transaction);
        buildHomeAddress(pid, transaction);
        buildMailingAddress(pid,transaction);
        buildDayPhoneNumber(pid, transaction);
        buildNightPhoneNumber(pid, transaction);
        buildMobilePhoneNumber(pid, transaction);
        buildUpdateTime(pid, transaction);
    }

    private void buildIdentifiers(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pid.getPid1_SetIDPID().setValue(FIXED_PID_ID);
        var patientIdentifier1 = pid.getPid3_PatientIdentifierList(0);
        if (utils.notNullNorBlank(transaction.getNHSNUMBER())) {
            var nhsNumber = utils.stripEmptyDoubleQuotes(transaction.getNHSNUMBER());
            patientIdentifier1.getCx1_IDNumber().setValue(nhsNumber);
            patientIdentifier1.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(NHS_IDENTIFIER_AUTHORITY);
            patientIdentifier1.getCx5_IdentifierTypeCode().setValue(NHS_IDENTIFIER_TYPE_CODE);
        }

        var patientIdentifier2 = pid.getPid3_PatientIdentifierList(1);
        patientIdentifier2.getCx1_IDNumber().setValue(utils.stripEmptyDoubleQuotes(transaction.getUNITNUMBER()));
        if (utils.notNullNorBlank(transaction.getUNITNUMBER())) {
            patientIdentifier2.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(
                    utils.stripEmptyDoubleQuotes(transaction.getSYSTEMID())
            );
            patientIdentifier2.getCx5_IdentifierTypeCode().setValue(PATIENT_IDENTIFIER_TYPE_CODE);
        }
    }

    private void buildGenericData(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pid.getPid6_MotherSMaidenName(0).getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(transaction.getMAIDENNAME()));
        pid.getPid7_DateTimeOfBirth().getTs1_Time().setValue(utils.mapDateFormat(transaction.getBIRTHDATE()));
        pid.getPid8_AdministrativeSex().setValue(utils.stripEmptyDoubleQuotes(transaction.getSEX()));
        pid.getPid9_PatientAlias(0).getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(transaction.getALIASSURNAME()));
        pid.getPid9_PatientAlias(0).getXpn2_GivenName().setValue(utils.stripEmptyDoubleQuotes(transaction.getALIASFORENAME()));
        pid.getPid16_MaritalStatus().getCe1_Identifier().setValue(transaction.getMARITALSTATUS());
        pid.getPid17_Religion().getCe1_Identifier().setValue(transaction.getRELIGIONSTATUS());
        pid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue(transaction.getETHNICORIGIN());
        pid.getPid29_PatientDeathDateAndTime().getTs1_Time().setValue(utils.mapDateFormat(transaction.getDEATHDATE()));
        pid.getPid30_PatientDeathIndicator().setValue(
                utils.notNullNorBlank(transaction.getDEATHDATE()) ? "Y" : "N"
        );
        pid.getPid32_IdentityReliabilityCode(0).setValue(utils.stripEmptyDoubleQuotes(transaction.getNHSCERTIFICATION()));
    }

    private void buildHomeAddress(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        XAD patientAddress = pid.getPid11_PatientAddress(0);
        patientAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(utils.stripEmptyDoubleQuotes(transaction.getADDRESS1()));
        patientAddress.getXad2_OtherDesignation().setValue(utils.stripEmptyDoubleQuotes(transaction.getADDRESS2()));
        patientAddress.getXad3_City().setValue(utils.stripEmptyDoubleQuotes(transaction.getADDRESS3()));
        if (utils.notNullNorBlank(transaction.getADDRESS4()) && utils.notNullNorBlank(transaction.getADDRESS5())) {
            patientAddress.getXad4_StateOrProvince().setValue(transaction.getADDRESS4() + "," + transaction.getADDRESS5());
        }
        patientAddress.getXad5_ZipOrPostalCode().setValue(utils.stripEmptyDoubleQuotes(transaction.getPOSTCODE()));
        patientAddress.getXad7_AddressType().setValue(HL7Constants.ADDRESS_TYPE_HOME);
    }

    private void buildMailingAddress(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var patientAddress2 = pid.getPid11_PatientAddress(1);
        patientAddress2.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(
                utils.stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS1())
        );
        patientAddress2.getXad2_OtherDesignation().setValue(utils.stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS2()));
        patientAddress2.getXad3_City().setValue(utils.stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS3()));
        if (utils.notNullNorBlank(transaction.getCONTACTADDRESS4()) && utils.notNullNorBlank(transaction.getCONTACTADDRESS5())) {
            patientAddress2.getXad4_StateOrProvince().setValue(transaction.getCONTACTADDRESS4() + "," + transaction.getCONTACTADDRESS5());
        }
        patientAddress2.getXad5_ZipOrPostalCode().setValue(utils.stripEmptyDoubleQuotes(transaction.getCONTACTADDRESS6()));
        patientAddress2.getXad7_AddressType().setValue(HL7Constants.ADDRESS_TYPE_MAILING);
    }

    private void buildMobilePhoneNumber(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var phoneNumber3 = pid.getPid13_PhoneNumberHome(1);
        phoneNumber3.getXtn1_TelephoneNumber().setValue(utils.stripEmptyDoubleQuotes(transaction.getMOBILE()));
        if (utils.notNullNorBlank(transaction.getMOBILE())) {
            phoneNumber3.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PERSONAL);
            phoneNumber3.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_MOBILE);
            phoneNumber3.getXtn9_AnyText().setValue(HL7Constants.TELECOM_MOBILE);
        }
    }

    private void buildNightPhoneNumber(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var phoneNumber2 = pid.getPid13_PhoneNumberHome(1);
        phoneNumber2.getXtn1_TelephoneNumber().setValue(utils.stripEmptyDoubleQuotes(transaction.getTELEPHONENIGHT()));
        if (utils.notNullNorBlank(transaction.getTELEPHONENIGHT())) {
            phoneNumber2.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber2.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber2.getXtn9_AnyText().setValue(HL7Constants.TELECOM_NIGHT);
        }
    }

    private void buildDayPhoneNumber(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var phoneNumber1 = pid.getPid13_PhoneNumberHome(0);
        phoneNumber1.getXtn1_TelephoneNumber().setValue(utils.stripEmptyDoubleQuotes(transaction.getTELEPHONEDAY()));
        phoneNumber1.getXtn4_EmailAddress().setValue(utils.stripEmptyDoubleQuotes(transaction.getEMAIL()));
        if (utils.notNullNorBlank(transaction.getTELEPHONEDAY())) {
            phoneNumber1.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber1.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber1.getXtn9_AnyText().setValue(HL7Constants.TELECOM_DAY);
        }
    }

    private void buildPatientName(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var patientName = pid.getPid5_PatientName(0);
        patientName.getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(transaction.getSURNAME()));
        patientName.getXpn2_GivenName().setValue(readGivenName(transaction.getFORENAME()));
        patientName.getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(readMiddleNames(transaction.getFORENAME()));

        patientName.getXpn5_PrefixEgDR().setValue(utils.stripEmptyDoubleQuotes(transaction.getTITLE()));
    }

    private void buildUpdateTime(PID pid, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pid.getPid33_LastUpdateDateTime().getTs1_Time().setValue(utils.mapDateTimeFormat(transaction.getUPDATEDATE()));
    }

    public String readGivenName(String forename) {
        if (utils.notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(0, spacePosition);
            } else {
                return forename;
            }
        }
        return "";
    }

    public String readMiddleNames(String forename) {
        if (utils.notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(spacePosition);
            }
        }
        return "";
    }
}