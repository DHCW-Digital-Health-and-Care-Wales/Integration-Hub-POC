package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.datatype.XTN;
import ca.uhn.hl7v2.model.v251.segment.NK1;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.WpasMapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class Nk1Mapper {
    private WpasMapUtils utils;

    public Nk1Mapper() {
        utils = new WpasMapUtils();
    }

    public void buildNK1(NK1 nk1, MAINDATA.TRANSACTION transaction, int setId,
                         String name, String relCode, String relText,
                         String addr1, String addr2, String addr3, String addr4, String postcode,
                         String telDay, String telNight) throws DataTypeException {

        nk1.getNk11_SetIDNK1().setValue(String.valueOf(setId));
        nk1.getNk12_Name(0).getXpn1_FamilyName().getFn1_Surname().setValue(utils.stripEmptyDoubleQuotes(name));
        nk1.getNk13_Relationship().getCe1_Identifier().setValue(utils.stripEmptyDoubleQuotes(relCode));
        nk1.getNk13_Relationship().getCe2_Text().setValue(utils.stripEmptyDoubleQuotes(relText));

        XAD nkAddress = nk1.getNk14_Address(0);
        nkAddress.getXad1_StreetAddress().getSad1_StreetOrMailingAddress().setValue(utils.stripEmptyDoubleQuotes(addr1));
        nkAddress.getXad2_OtherDesignation().setValue(utils.stripEmptyDoubleQuotes(addr2));
        nkAddress.getXad3_City().setValue(utils.stripEmptyDoubleQuotes(addr3));
        nkAddress.getXad4_StateOrProvince().setValue(utils.stripEmptyDoubleQuotes(addr4));
        nkAddress.getXad5_ZipOrPostalCode().setValue(utils.stripEmptyDoubleQuotes(postcode));

        setPhoneNumber(nk1, 0, telDay, HL7Constants.TELECOM_DAY);
        setPhoneNumber(nk1, 1, telNight, HL7Constants.TELECOM_NIGHT);
    }

    private void setPhoneNumber(NK1 nk1, int index, String phoneNumber, String anyText) throws DataTypeException {
        XTN phone = nk1.getNk15_PhoneNumber(index);
        if (utils.notNullNorBlank(phoneNumber)) {
            phone.getXtn1_TelephoneNumber().setValue(phoneNumber);
            phone.getXtn2_TelecommunicationUseCode().setValue(HL7Constants.TELECOM_USE_PRIMARY_RESIDENCE_NUMBER);
            phone.getXtn3_TelecommunicationEquipmentType().setValue(HL7Constants.TELECOM_EQUPIMENT_CODE_PHONE);
            phone.getXtn9_AnyText().setValue(anyText);
        }
    }

    public void build1stNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        buildNK1(nk1, transaction, 1,
                transaction.getNEXTKIN(), transaction.getNEXTKINRELCODE(), transaction.getNEXTKINREL(),
                transaction.getNEXTKINADDR1(), transaction.getNEXTKINADDR2(), transaction.getNEXTKINADDR3(), transaction.getNEXTKINADDR4(), transaction.getNEXTKINPOSTCODE(),
                transaction.getNEXTKINTELDAY(), transaction.getNEXTKINTELNIGHT());
    }

    public void build2ndNK1(NK1 nk1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        buildNK1(nk1, transaction, 2,
                transaction.getOTHERKIN(), transaction.getOTHERKINRELCODE(), transaction.getOTHERKINREL(),
                transaction.getOTHERKINADDR1(), transaction.getOTHERKINADDR2(), transaction.getOTHERKINADDR3(), transaction.getOTHERKINADDR4(), transaction.getOTHERKINPOSTCODE(),
                transaction.getOTHERKINTELDAY(), transaction.getOTHERKINTELNIGHT());
    }
}