package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.EvnMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MshMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pd1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.PidMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Nk1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pv1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pv2Mapper;

import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;

public class IpiToAdtA01Mapper {

    private final MshMapper mshMapper;
    private final EvnMapper evnMapper;
    private final PidMapper pidMapper;
    private final Pd1Mapper pd1Mapper;
    private final Nk1Mapper nk1Mapper;
    private final Pv1Mapper pv1Mapper;
    private final Pv2Mapper pv2Mapper;


    public IpiToAdtA01Mapper(DateTimeProvider dateTimeProvider) {

        mshMapper = new MshMapper(dateTimeProvider);
        evnMapper = new EvnMapper(dateTimeProvider);
        pidMapper = new PidMapper();
        pd1Mapper = new Pd1Mapper();
        nk1Mapper = new Nk1Mapper();
        pv1Mapper = new Pv1Mapper();
        pv2Mapper = new Pv2Mapper();
    }

    public ADT_A01 mapIpiToAdtA01(WpasData wpasData) throws DataTypeException {
        var a01 = new ADT_A01();
        mshMapper.buildMsh(a01.getMSH(), wpasData.getMaindata().getTRANSACTION(), getA01TypeData());
        evnMapper.buildEvn(a01.getEVN(), wpasData);
        pidMapper.buildPid(a01.getPID(), wpasData.getMaindata().getTRANSACTION());
        pd1Mapper.buildPD1(a01.getPD1(), wpasData.getMaindata().getTRANSACTION());
        nk1Mapper.build1stNK1(a01.getNK1(0), wpasData.getMaindata().getTRANSACTION());
        nk1Mapper.build2ndNK1(a01.getNK1(1), wpasData.getMaindata().getTRANSACTION());
        pv1Mapper.buildPV1(a01.getPV1(), wpasData.getMaindata().getTRANSACTION());
        pv2Mapper.buildPV2(a01.getPV2(), wpasData.getMaindata().getTRANSACTION());

        return a01;
    }

    private static Hl7MessageTypeData getA01TypeData() {
        return new Hl7MessageTypeData(HL7Constants.ADT, HL7Constants.A01, "ADT_A01");
    }

}
