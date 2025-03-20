package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.EvnMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MshMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Nk1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pd1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.PidMapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

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

    public ADT_A05 mapMpiToAdtA28(MAINDATA.TRANSACTION transaction) throws DataTypeException {
        var a28 = new ADT_A05();
        Hl7MessageTypeData messageTypeData = new Hl7MessageTypeData("ADT", "A28", "ADT_A05");
        mshMapper.buildMsh(a28.getMSH(), transaction, messageTypeData);
        evnMapper.buildEvn(a28.getEVN(), transaction);
        pidMapper.buildPid(a28.getPID(), transaction);
        pd1Mapper.buildPD1(a28.getPD1(), transaction);
        nk1Mapper.build1stNK1(a28.getNK1(0), transaction);
        nk1Mapper.build2ndNK1(a28.getNK1(1), transaction);

        return a28;
    }
}
