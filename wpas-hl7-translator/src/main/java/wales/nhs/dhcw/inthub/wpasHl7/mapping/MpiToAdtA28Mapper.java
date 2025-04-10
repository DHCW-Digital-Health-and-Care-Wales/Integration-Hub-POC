package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.EvnMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MshMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Nk1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pd1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.PidMapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;

public class MpiToAdtA28Mapper {

    private final MshMapper mshMapper;
    private final EvnMapper evnMapper;
    private final PidMapper pidMapper;
    private final Pd1Mapper pd1Mapper;
    private final Nk1Mapper nk1Mapper;

    public MpiToAdtA28Mapper(DateTimeProvider dateTimeProvider) {
        mshMapper = new MshMapper(dateTimeProvider);
        evnMapper = new EvnMapper(dateTimeProvider);
        pidMapper = new PidMapper();
        pd1Mapper = new Pd1Mapper();
        nk1Mapper = new Nk1Mapper();
    }

    public ADT_A05 mapMpiToAdtA28(WpasData wpasData) throws DataTypeException {
        var a28 = new ADT_A05();
        var transaction = wpasData.getMaindata().getTRANSACTION();
        Hl7MessageTypeData messageTypeData = new Hl7MessageTypeData("ADT", "A28", "ADT_A05");
        mshMapper.buildMsh(a28.getMSH(), transaction, messageTypeData, transaction.getDHACODE());
        evnMapper.buildEvn(a28.getEVN(), wpasData);
        pidMapper.buildPid(a28.getPID(), wpasData.getMaindata().getTRANSACTION());
        pd1Mapper.buildPD1(a28.getPD1(), wpasData.getMaindata().getTRANSACTION());
        nk1Mapper.build1stNK1(a28.getNK1(0), wpasData.getMaindata().getTRANSACTION());
        nk1Mapper.build2ndNK1(a28.getNK1(1), wpasData.getMaindata().getTRANSACTION());

        return a28;
    }
}
