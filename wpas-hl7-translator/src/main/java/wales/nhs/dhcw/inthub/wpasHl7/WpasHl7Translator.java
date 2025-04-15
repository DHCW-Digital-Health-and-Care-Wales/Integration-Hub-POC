package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.IpiToAdtA01Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.IptToAdtA02Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MpiToAdtA28Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MprToAdtA40Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MpaToAdtA31Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;

public class WpasHl7Translator {
    public static final String HL7_VERSION = "2.5.1";

    private final MpiToAdtA28Mapper mpiToAdtA28Mapper;
    private final MprToAdtA40Mapper mprToAdtA39Mapper;
    private final MpaToAdtA31Mapper mpaToAdtA31Mapper;
    private final IpiToAdtA01Mapper ipiToAdtA01Mapper;
    private final IptToAdtA02Mapper iptToAdtA02Mapper;


    public WpasHl7Translator(DateTimeProvider dateTimeProvider) {
        mpiToAdtA28Mapper = new MpiToAdtA28Mapper(dateTimeProvider);
        mprToAdtA39Mapper = new MprToAdtA40Mapper(dateTimeProvider);
        mpaToAdtA31Mapper = new MpaToAdtA31Mapper(dateTimeProvider);
        ipiToAdtA01Mapper = new IpiToAdtA01Mapper(dateTimeProvider);
        iptToAdtA02Mapper = new IptToAdtA02Mapper(dateTimeProvider);
    }

    AbstractMessage translate(WpasData wpasData) throws DataTypeException {
        switch (wpasData.getMaindata().getTRANSACTION().getMSGID()) {
            case "MPI":
                return mpiToAdtA28Mapper.mapMpiToAdtA28(wpasData);
            case "MPR":
                return mprToAdtA39Mapper.mapMprToA40(wpasData);
            case "MPA":
                return mpaToAdtA31Mapper.mapMpaToAdtA31(wpasData);
            case "IPI":
                return ipiToAdtA01Mapper.mapIpiToAdtA01(wpasData);
            case "IPT":
                return iptToAdtA02Mapper.mapIptToAdtA02(wpasData);
            default:
                throw new RuntimeException("Unknown message type: " + wpasData.getMaindata().getTRANSACTION().getMSGID());
        }
    }
}
