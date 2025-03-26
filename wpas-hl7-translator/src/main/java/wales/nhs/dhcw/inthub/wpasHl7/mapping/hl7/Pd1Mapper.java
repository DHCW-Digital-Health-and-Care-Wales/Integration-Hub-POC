package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.PD1;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class Pd1Mapper {
    public Pd1Mapper() {
    }

    public void buildPD1(PD1 pd1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pd1.getPd13_PatientPrimaryFacility(0).getXon1_OrganizationName().setValue(transaction.getGPADDR1());
        pd1.getPd13_PatientPrimaryFacility(0).getXon10_OrganizationIdentifier().setValue(transaction.getGPPRACTICE());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn1_IDNumber().setValue(transaction.getREGISTEREDGP());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getGPSURNAME());
        pd1.getPd14_PatientPrimaryCareProviderNameIDNo(0).getXcn3_GivenName().setValue(transaction.getGPINITS());
    }
}