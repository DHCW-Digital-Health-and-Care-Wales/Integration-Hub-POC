package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.message.ADT_A39;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.EvnMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MgrMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MshMapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Pd1Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.PidMapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class MprToAdtA40Mapper {
    private final ADT_A39 adtMessage;
    private final MshMapper mshMapper;
    private final EvnMapper evnMapper;
    private final PidMapper pidMapper;
    private final Pd1Mapper pd1Mapper;
    private final MgrMapper mgrMapper = new MgrMapper();

    public MprToAdtA40Mapper(DateTimeProvider dateTimeProvider) {
        adtMessage = new ADT_A39();

        mshMapper = new MshMapper(dateTimeProvider);
        evnMapper = new EvnMapper(dateTimeProvider);
        pidMapper = new PidMapper();
        pd1Mapper = new Pd1Mapper();
    }

    public ADT_A39 mapMprToA40(MAINDATA.TRANSACTION transaction) throws DataTypeException {

        mshMapper.buildMsh(adtMessage.getMSH(), transaction, getA40TypeData());
        evnMapper.buildEvn(adtMessage.getEVN(), transaction);
        pidMapper.buildPid(adtMessage.getPATIENT().getPID(), transaction);
        pd1Mapper.buildPD1(adtMessage.getPATIENT().getPD1(), transaction);
        mgrMapper.MRGMapper(adtMessage.getPATIENT().getMRG(), transaction);
        return adtMessage;
    }

    private static Hl7MessageTypeData getA40TypeData() {
        return new Hl7MessageTypeData(HL7Constants.ADT, HL7Constants.A40, "ADT_A39");
    }
}

