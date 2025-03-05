package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import wales.nhs.dhcw.inthub.wpasHl7.xml.MAINDATA;

public class WpasHl7Translator {
    public static final String HL7_VERSION = "2.5.1";

    private final MpiToAdtA28Mapper mpiToAdtA28Mapper;


    public WpasHl7Translator(DateTimeProvider dateTimeProvider) {
        mpiToAdtA28Mapper = new MpiToAdtA28Mapper(dateTimeProvider);
    }

    AbstractMessage translate(MAINDATA wpasXml) throws DataTypeException {
        switch (wpasXml.getTRANSACTION().getMSGID()) {
            case "MPI":
                return mpiToAdtA28Mapper.translateMpiToAdtA28(wpasXml.getTRANSACTION());
            default:
                throw new RuntimeException("Unknown message type: " + wpasXml.getTRANSACTION().getMSGID());
        }
    }
}
