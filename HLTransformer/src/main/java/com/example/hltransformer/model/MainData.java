package com.example.hltransformer.model;


import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@Data
@XmlRootElement(name = "MAINDATA", namespace = "http://PAS_Demographics")
@XmlAccessorType(XmlAccessType.FIELD)
public class MainData {

    @XmlElement(name = "TRANSACTION", namespace = "http://PAS_Demographics")
    private Transaction transaction;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Transaction {
        @XmlElement(name = "TRANSACTION_ID", namespace = "http://PAS_Demographics")
        private String transactionId;

        @XmlElement(name = "MSG_ID", namespace = "http://PAS_Demographics")
        private String msgId;

        @XmlElement(name = "UNIT_NUMBER", namespace = "http://PAS_Demographics")
        private String unitNumber;

        @XmlElement(name = "NHS_NUMBER", namespace = "http://PAS_Demographics")
        private String nhsNumber;

        @XmlElement(name = "SURNAME", namespace = "http://PAS_Demographics")
        private String surname;

        @XmlElement(name = "FORENAME", namespace = "http://PAS_Demographics")
        private String forename;

        @XmlElement(name = "TITLE", namespace = "http://PAS_Demographics")
        private String title;

        @XmlElement(name = "BIRTHDATE", namespace = "http://PAS_Demographics")
        private String birthdate;

        @XmlElement(name = "DEATHDATE", namespace = "http://PAS_Demographics")
        private String deathdate;

        @XmlElement(name = "SEX", namespace = "http://PAS_Demographics")
        private String sex;

        @XmlElement(name = "ADDRESS_1", namespace = "http://PAS_Demographics")
        private String address1;

        @XmlElement(name = "ADDRESS_2", namespace = "http://PAS_Demographics")
        private String address2;

        @XmlElement(name = "ADDRESS_3", namespace = "http://PAS_Demographics")
        private String address3;

        @XmlElement(name = "ADDRESS_4", namespace = "http://PAS_Demographics")
        private String address4;

        @XmlElement(name = "ADDRESS_5", namespace = "http://PAS_Demographics")
        private String address5;

        @XmlElement(name = "POSTCODE", namespace = "http://PAS_Demographics")
        private String postcode;

        @XmlElement(name = "TELEPHONE_DAY", namespace = "http://PAS_Demographics")
        private String telephoneDay;

        @XmlElement(name = "TELEPHONE_NIGHT", namespace = "http://PAS_Demographics")
        private String telephoneNight;

        @XmlElement(name = "OVERSEAS_STATUS", namespace = "http://PAS_Demographics")
        private Integer overseasStatus;

        @XmlElement(name = "MARITAL_STATUS", namespace = "http://PAS_Demographics")
        private Integer maritalStatus;

        @XmlElement(name = "DISABLED_STATUS", namespace = "http://PAS_Demographics")
        private Integer disabledStatus;

        @XmlElement(name = "ETHNIC_ORIGIN", namespace = "http://PAS_Demographics")
        private String ethnicOrigin;

        @XmlElement(name = "RELIGION_STATUS", namespace = "http://PAS_Demographics")
        private String religionStatus;

        @XmlElement(name = "REGISTERED_GP", namespace = "http://PAS_Demographics")
        private String registeredGp;

        @XmlElement(name = "GP_PRACTICE", namespace = "http://PAS_Demographics")
        private String gpPractice;

        @XmlElement(name = "NHS_CERTIFICATION", namespace = "http://PAS_Demographics")
        private String nhsCertification;

        @XmlElement(name = "GP_SURNAME", namespace = "http://PAS_Demographics")
        private String gpSurname;

        @XmlElement(name = "GP_INITS", namespace = "http://PAS_Demographics")
        private String gpInits;

        @XmlElement(name = "GP_ADDR1", namespace = "http://PAS_Demographics")
        private String gpAddr1;

        @XmlElement(name = "GP_ADDR2", namespace = "http://PAS_Demographics")
        private String gpAddr2;

        @XmlElement(name = "GP_ADDR3", namespace = "http://PAS_Demographics")
        private String gpAddr3;

        @XmlElement(name = "GP_ADDR4", namespace = "http://PAS_Demographics")
        private String gpAddr4;

        @XmlElement(name = "GP_POSTCODE", namespace = "http://PAS_Demographics")
        private String gpPostcode;

        @XmlElement(name = "GP_TELEPHONE", namespace = "http://PAS_Demographics")
        private String gpTelephone;

        @XmlElement(name = "GP_FAX", namespace = "http://PAS_Demographics")
        private String gpFax;

        @XmlElement(name = "MOBILE", namespace = "http://PAS_Demographics")
        private String mobile;

        @XmlElement(name = "EMAIL", namespace = "http://PAS_Demographics")
        private String email;

        @XmlElement(name = "DHA_CODE", namespace = "http://PAS_Demographics")
        private String dhaCode;

        @XmlElement(name = "NEXT_KIN", namespace = "http://PAS_Demographics")
        private String nextKin;

        @XmlElement(name = "NEXT_KIN_REL", namespace = "http://PAS_Demographics")
        private String nextKinRel;

        @XmlElement(name = "NEXT_KIN_ADDR1", namespace = "http://PAS_Demographics")
        private String nextKinAddr1;

        @XmlElement(name = "NEXT_KIN_ADDR2", namespace = "http://PAS_Demographics")
        private String nextKinAddr2;

        @XmlElement(name = "NEXT_KIN_ADDR3", namespace = "http://PAS_Demographics")
        private String nextKinAddr3;

        @XmlElement(name = "NEXT_KIN_ADDR4", namespace = "http://PAS_Demographics")
        private String nextKinAddr4;

        @XmlElement(name = "NEXT_KIN_POSTCODE", namespace = "http://PAS_Demographics")
        private String nextKinPostcode;

        @XmlElement(name = "OTHER_KIN", namespace = "http://PAS_Demographics")
        private String otherKin;

        @XmlElement(name = "OTHER_KIN_REL", namespace = "http://PAS_Demographics")
        private String otherKinRel;

        @XmlElement(name = "OTHER_KIN_ADDR1", namespace = "http://PAS_Demographics")
        private String otherKinAddr1;

        @XmlElement(name = "OTHER_KIN_ADDR2", namespace = "http://PAS_Demographics")
        private String otherKinAddr2;

        @XmlElement(name = "OTHER_KIN_ADDR3", namespace = "http://PAS_Demographics")
        private String otherKinAddr3;

        @XmlElement(name = "OTHER_KIN_ADDR4", namespace = "http://PAS_Demographics")
        private String otherKinAddr4;

        @XmlElement(name = "OTHER_KIN_POSTCODE", namespace = "http://PAS_Demographics")
        private String otherKinPostcode;

        @XmlElement(name = "NEXT_KIN_TEL_DAY", namespace = "http://PAS_Demographics")
        private String nextKinTelDay;

        @XmlElement(name = "NEXT_KIN_TEL_NIGHT", namespace = "http://PAS_Demographics")
        private String nextKinTelNight;

        @XmlElement(name = "OTHER_KIN_TEL_DAY", namespace = "http://PAS_Demographics")
        private String otherKinTelDay;

        @XmlElement(name = "OTHER_KIN_TEL_NIGHT", namespace = "http://PAS_Demographics")
        private String otherKinTelNight;

        @XmlElement(name = "MAIDEN_NAME", namespace = "http://PAS_Demographics")
        private String maidenName;

        @XmlElement(name = "ALIAS_FORENAME", namespace = "http://PAS_Demographics")
        private String aliasForename;

        @XmlElement(name = "ALIAS_SURNAME", namespace = "http://PAS_Demographics")
        private String aliasSurname;

        @XmlElement(name = "REQUESTED_NAME", namespace = "http://PAS_Demographics")
        private String requestedName;

        @XmlElement(name = "OCCUPATION", namespace = "http://PAS_Demographics")
        private String occupation;

        @XmlElement(name = "CONTACT_ADDRESS_1", namespace = "http://PAS_Demographics")
        private String contactAddress1;

        @XmlElement(name = "CONTACT_ADDRESS_2", namespace = "http://PAS_Demographics")
        private String contactAddress2;

        @XmlElement(name = "CONTACT_ADDRESS_3", namespace = "http://PAS_Demographics")
        private String contactAddress3;

        @XmlElement(name = "CONTACT_ADDRESS_4", namespace = "http://PAS_Demographics")
        private String contactAddress4;

        @XmlElement(name = "CONTACT_ADDRESS_5", namespace = "http://PAS_Demographics")
        private String contactAddress5;

        @XmlElement(name = "CONTACT_ADDRESS_6", namespace = "http://PAS_Demographics")
        private String contactAddress6;

        @XmlElement(name = "EVENT_CODES", namespace = "http://PAS_Demographics")
        private String eventCodes;

        @XmlElement(name = "KEY_NOTE_TYPES", namespace = "http://PAS_Demographics")
        private String keyNoteTypes;

        @XmlElement(name = "OTHER_ALIAS", namespace = "http://PAS_Demographics")
        private String otherAlias;

        @XmlElement(name = "GENDER", namespace = "http://PAS_Demographics")
        private String gender;

        @XmlElement(name = "PREFERRED_LANGUAGE", namespace = "http://PAS_Demographics")
        private String preferredLanguage;

        @XmlElement(name = "UPDATE_DATE", namespace = "http://PAS_Demographics")
        private String updateDate;

        @XmlElement(name = "USER_ID", namespace = "http://PAS_Demographics")
        private String userId;

        @XmlElement(name = "SYSTEM_ID", namespace = "http://PAS_Demographics")
        private String systemId;

        @XmlElement(name = "NEW_UNIT_NUMBER", namespace = "http://PAS_Demographics")
        private Long newUnitNumber;

        @XmlElement(name = "OLD_UNIT_NUMBER", namespace = "http://PAS_Demographics")
        private Long oldUnitNumber;
    }

}
