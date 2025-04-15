package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.CX;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.PatientVisitType;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.WpasMapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;


public class Pv1Mapper {
    private final WpasMapUtils utils;

    public Pv1Mapper() {
        utils = new WpasMapUtils();
    }

    public void buildPV1(PV1 pv1, MAINDATA.TRANSACTION transaction, PatientVisitType visitType) throws DataTypeException {
        pv1.getPv11_SetIDPV1().setValue("1");
        pv1.getPv12_PatientClass().setValue(visitType.getCode());

        pv1.getPv13_AssignedPatientLocation().getPl1_PointOfCare().setValue(transaction.getWARD());
        pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().setValue(transaction.getCURLOCPROVIDERCODE());
        pv1.getPv13_AssignedPatientLocation().getPl9_LocationDescription().setValue(transaction.getWARDNAME());

        pv1.getPv14_AdmissionType().setValue(transaction.getSPELLNO());

        pv1.getPv16_PriorPatientLocation().getPl1_PointOfCare().setValue(transaction.getADMWARD());
        pv1.getPv16_PriorPatientLocation().getPl9_LocationDescription().setValue(transaction.getADMWARDNAME());
        setupPv19(pv1,transaction,0);

        pv1.getPv110_HospitalService().setValue(transaction.getSPEC());

        String admitSource = transaction.getSOURCEADMISSION();
        if (admitSource != null && admitSource.contains(" ")) {
            admitSource = admitSource.split(" ")[0];
        }
        pv1.getPv114_AdmitSource().setValue(admitSource);
        setupPv117(pv1,transaction,0);
        pv1.getPv118_PatientType().setValue(transaction.getTRTTYPE());

        setupPv119(pv1,transaction);

        pv1.getPv136_DischargeDisposition().setValue(transaction.getDISDEST());
        pv1.getPv137_DischargedToLocation().getDld1_DischargeLocation().setValue(utils.stripEmptyDoubleQuotes(transaction.getDISDESTDESC()));

        if (transaction.getTRTDATE() != null && transaction.getTRTTIME() != null) {
            pv1.getPv144_AdmitDateTime().getTime().setValue(Hl7DateFormatProvider.getFormatDateTime(transaction.getTRTDATE()+transaction.getTRTTIME()));
        }

        if (transaction.getDISDATE() != null && transaction.getDISTIME() != null) {
            pv1.getPv145_DischargeDateTime(0).getTs1_Time().setValue(Hl7DateFormatProvider.getFormatDateTime(transaction.getDISDATE()+transaction.getDISTIME()));
        }

        pv1.getPv150_AlternateVisitID().getCx1_IDNumber().setValue(transaction.getUPI());
    }

    private void setupPv117(PV1 pv1, MAINDATA.TRANSACTION transaction,int index) throws DataTypeException {
        pv1.getPv117_AdmittingDoctor(index).getXcn1_IDNumber().setValue(utils.stripEmptyDoubleQuotes(transaction.getCONSGMC()));
        pv1.getPv117_AdmittingDoctor(index).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getADMCONSNAME());
        pv1.getPv117_AdmittingDoctor(index).getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(transaction.getADMCONS());
    }

    private void setupPv19(PV1 pv1, MAINDATA.TRANSACTION transaction,int index) throws DataTypeException {
        pv1.getPv19_ConsultingDoctor(index).getXcn1_IDNumber().setValue(transaction.getCONSGMC());
        pv1.getPv19_ConsultingDoctor(index).getXcn2_FamilyName().getFn1_Surname().setValue(transaction.getCONSNAME());
        pv1.getPv19_ConsultingDoctor(index).getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().setValue(transaction.getCONS());
    }

    private void setupPv119(PV1 pv1, MAINDATA.TRANSACTION transaction) throws DataTypeException {
        CX visitNumber = pv1.getPv119_VisitNumber();
        visitNumber.getCx1_IDNumber().setValue(transaction.getUNIQUEID());
        visitNumber.getCx4_AssigningAuthority().getHd1_NamespaceID().setValue(transaction.getSYSTEMID());
        visitNumber.getCx5_IdentifierTypeCode().setValue("VN");
    }
}
