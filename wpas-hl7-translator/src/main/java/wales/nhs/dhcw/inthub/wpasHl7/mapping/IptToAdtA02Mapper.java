package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A02;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.EvnMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MshMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pd1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.PidMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pv1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pv2Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;

public class IptToAdtA02Mapper {

    private final MshMapper mshMapper;
    private final EvnMapper evnMapper;
    private final PidMapper pidMapper;
    private final Pd1Mapper pd1Mapper;
    private final Pv1Mapper pv1Mapper;
    private final Pv2Mapper pv2Mapper;

    public IptToAdtA02Mapper(DateTimeProvider dateTimeProvider) {
        mshMapper = new MshMapper(dateTimeProvider);
        evnMapper = new EvnMapper(dateTimeProvider);
        pidMapper = new PidMapper();
        pd1Mapper = new Pd1Mapper();
        pv1Mapper = new Pv1Mapper();
        pv2Mapper = new Pv2Mapper();
    }

    public ADT_A02 mapIptToAdtA02(WpasData wpasData) throws DataTypeException {
        var a02 = new ADT_A02();
        var transaction = wpasData.getMaindata().getTRANSACTION();
        var messageTypeData = new Hl7MessageTypeData("ADT", "A02", "ADT_A02");
        var sendingFacility = transaction.getCURLOCPROVIDERCODE();
        mshMapper.buildMsh(a02.getMSH(), transaction, messageTypeData, sendingFacility);
        evnMapper.buildEvn(a02.getEVN(), wpasData);
        pidMapper.buildPid(a02.getPID(), transaction);
        pd1Mapper.buildPD1(a02.getPD1(), transaction);
        pv1Mapper.buildPV1(a02.getPV1(), transaction, PatientVisitType.INPATIENT);
        pv2Mapper.buildPV2(a02.getPV2(), transaction);
        return a02;
    }
}
