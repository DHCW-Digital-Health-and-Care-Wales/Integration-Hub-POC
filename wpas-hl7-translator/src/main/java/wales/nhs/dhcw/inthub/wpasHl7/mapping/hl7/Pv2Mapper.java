package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.PV2;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.WpasMapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class Pv2Mapper {

    private final WpasMapUtils utils;

    public Pv2Mapper() {
        utils = new WpasMapUtils();
    }

    public void buildPV2(PV2 pv2, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        pv2.getPv212_VisitDescription().setValue(transaction.getSPECNAME());
        pv2.getPv224_PatientStatusCode().setValue(utils.stripEmptyDoubleQuotes(transaction.getOUTCOME()));

        String spellNo = transaction.getSPELLNO();
        if (spellNo != null && spellNo.contains(" ")) {
            String[] parts = spellNo.split(" ", 2);
            pv2.getPv23_AdmitReason().getCe1_Identifier().setValue(parts[0]);
            pv2.getPv23_AdmitReason().getCe2_Text().setValue(parts[1]);
        } else if (spellNo != null) {
            pv2.getPv23_AdmitReason().getCe1_Identifier().setValue(spellNo);
        }
    }
}

