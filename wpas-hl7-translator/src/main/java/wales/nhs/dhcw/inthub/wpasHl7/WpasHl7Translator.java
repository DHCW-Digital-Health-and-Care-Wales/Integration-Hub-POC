package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MpiToAdtA28Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MprToAdtA40Mapper;
import wales.nhs.dhcw.inthub.wpasHl7.xml.QueueData;

public class WpasHl7Translator {
    public static final String HL7_VERSION = "2.5.1";

    private final MpiToAdtA28Mapper mpiToAdtA28Mapper;
    private final MprToAdtA40Mapper mprToAdtA39Mapper;


    public WpasHl7Translator(DateTimeProvider dateTimeProvider) {
        mpiToAdtA28Mapper = new MpiToAdtA28Mapper(dateTimeProvider);
        mprToAdtA39Mapper = new MprToAdtA40Mapper(dateTimeProvider);
    }

    AbstractMessage translate(QueueData queueData) throws DataTypeException {
        switch (queueData.getMaindata().getTRANSACTION().getMSGID()) {
            case "MPI":
                return mpiToAdtA28Mapper.mapMpiToAdtA28(queueData);
            case "MPR":
                return mprToAdtA39Mapper.mapMprToA40(queueData);

            default:
                throw new RuntimeException("Unknown message type: " + queueData.getMaindata().getTRANSACTION().getMSGID());
        }
    }
}
