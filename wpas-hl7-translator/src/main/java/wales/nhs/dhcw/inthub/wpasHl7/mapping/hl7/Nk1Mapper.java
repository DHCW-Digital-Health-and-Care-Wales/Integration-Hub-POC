package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.datatype.XTN;
import ca.uhn.hl7v2.model.v251.segment.NK1;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class Nk1Mapper {
    private MapUtils utils;

    public Nk1Mapper() {
        utils = new MapUtils();
    }

    public void build1stNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        nk1.getNk11_SetIDNK1().setValue("1");
        nk1.getNk12_Name(0).getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKIN()));
        nk1.getNk13_Relationship().getCe1_Identifier().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINRELCODE()));
        nk1.getNk13_Relationship().getCe2_Text().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINREL()));

        XAD nkAddress = nk1.getNk14_Address(0);
        nkAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINADDR1()));
        nkAddress.getXad2_OtherDesignation().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINADDR2()));
        nkAddress.getXad3_City().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINADDR3()));
        nkAddress.getXad4_StateOrProvince().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINADDR4()));
        nkAddress.getXad5_ZipOrPostalCode().setValue(utils.stripEmptyDoubleQuotes(transaction.getNEXTKINPOSTCODE()));

        XTN phoneNumber = nk1.getNk15_PhoneNumber(0);
        if (utils.notNullNorBlank(transaction.getNEXTKINTELDAY())) {
            phoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getNEXTKINTELDAY());
            phoneNumber.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber.getXtn9_AnyText().setValue(HL7Constants.TELECOM_DAY);
        }

        var nightPhoneNumber = nk1.getNk15_PhoneNumber(1);
        if (utils.notNullNorBlank(transaction.getNEXTKINTELNIGHT())) {
            nightPhoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getNEXTKINTELNIGHT());
            nightPhoneNumber.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            nightPhoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            nightPhoneNumber.getXtn9_AnyText().setValue(HL7Constants.TELECOM_NIGHT);
        }
    }

    public void build2ndNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        nk1.getNk11_SetIDNK1().setValue("2");
        nk1.getNk12_Name(0).getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKIN()));
        nk1.getNk13_Relationship().getCe1_Identifier().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINRELCODE()));
        nk1.getNk13_Relationship().getCe2_Text().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINREL()));

        XAD nkAddress = nk1.getNk14_Address(0);
        nkAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINADDR1()));
        nkAddress.getXad2_OtherDesignation().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINADDR2()));
        nkAddress.getXad3_City().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINADDR3()));
        nkAddress.getXad4_StateOrProvince().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINADDR4()));
        nkAddress.getXad5_ZipOrPostalCode().setValue(utils.stripEmptyDoubleQuotes(transaction.getOTHERKINPOSTCODE()));

        XTN phoneNumber = nk1.getNk15_PhoneNumber(0);
        if (utils.notNullNorBlank(transaction.getOTHERKINTELDAY())) {
            phoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getOTHERKINTELDAY());
            phoneNumber.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            phoneNumber.getXtn9_AnyText().setValue(HL7Constants.TELECOM_DAY);
        }

        var nightPhoneNumber = nk1.getNk15_PhoneNumber(1);
        if (utils.notNullNorBlank(transaction.getOTHERKINTELNIGHT())) {
            nightPhoneNumber.getXtn1_TelephoneNumber().setValue(transaction.getOTHERKINTELNIGHT());
            nightPhoneNumber.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            nightPhoneNumber.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            nightPhoneNumber.getXtn9_AnyText().setValue(HL7Constants.TELECOM_NIGHT);
        }

    }
}