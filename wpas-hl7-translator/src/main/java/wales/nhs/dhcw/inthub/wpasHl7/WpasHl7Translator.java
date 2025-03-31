package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MpiToAdtA28Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MprToAdtA40Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.MpaToAdtA31Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class WpasHl7Translator {
    public static final String HL7_VERSION = "2.5.1";

    private final MpiToAdtA28Mapper mpiToAdtA28Mapper;
    private final MprToAdtA40Mapper mprToAdtA39Mapper;
    private final MpaToAdtA31Mapper mpaToAdtA31Mapper;


    public WpasHl7Translator(DateTimeProvider dateTimeProvider) {
        mpiToAdtA28Mapper = new MpiToAdtA28Mapper(dateTimeProvider);
        mprToAdtA39Mapper = new MprToAdtA40Mapper(dateTimeProvider);
        mpaToAdtA31Mapper = new MpaToAdtA31Mapper(dateTimeProvider);
    }

    AbstractMessage translate(MAINDATA wpasXml) throws DataTypeException {
        switch (wpasXml.getTRANSACTION().getMSGID()) {
            case "MPI":
                return mpiToAdtA28Mapper.mapMpiToAdtA28(wpasXml.getTRANSACTION());
            case "MPR":
                return mprToAdtA39Mapper.mapMprToA40(wpasXml.getTRANSACTION());
            case "MPA":
                return mpaToAdtA31Mapper.mapMpaToAdtA31(wpasXml.getTRANSACTION());
            default:
                throw new RuntimeException("Unknown message type: " + wpasXml.getTRANSACTION().getMSGID());
        }
    }
}
