package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.MRG;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class MgrMapper {

    public void MRGMapper(MRG mrg, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var priorPatientIdentifierList = mrg.getMrg1_PriorPatientIdentifierList(0);
        priorPatientIdentifierList.getCx1_IDNumber().setValue(transaction.getOLDUNITNUMBER());
        priorPatientIdentifierList.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
        priorPatientIdentifierList.getCx5_IdentifierTypeCode().setValue("PI");
    }
}