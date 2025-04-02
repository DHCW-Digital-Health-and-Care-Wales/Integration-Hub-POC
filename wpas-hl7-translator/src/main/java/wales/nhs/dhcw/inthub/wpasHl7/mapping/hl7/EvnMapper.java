package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.EVN;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.xml.WpasData;


public class EvnMapper {

    private final DateTimeProvider dateTimeProvider;

    public EvnMapper(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    public void buildEvn(EVN evn, WpasData queueData) throws DataTypeException {
        evn.getEvn2_RecordedDateTime().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
        evn.getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(queueData.getMaindata().getTRANSACTION().getUSERID());

        if(queueData.getQueueDateTime()!=null) {
            evn.getEvn6_EventOccurred().getTs1_Time().setValue(Hl7DateFormatProvider.getSimpleDateTimeFormatter().format(queueData.getQueueDateTime()));
        }
    }
}