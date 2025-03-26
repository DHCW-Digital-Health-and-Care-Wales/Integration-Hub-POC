package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.EVN;
import wales.nhs.dhcw.inthub.wpasHl7.DateTimeProvider;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.MapUtils;
import wales.nhs.dhcw.inthub.wpasHl7.xml.QueueData;

public class EvnMapper {

    private final DateTimeProvider dateTimeProvider;
    private final MapUtils utils;

    public EvnMapper(DateTimeProvider dateTimeProvider) {
        utils = new MapUtils();
        this.dateTimeProvider = dateTimeProvider;
    }

    public void buildEvn(EVN evn, QueueData queueData) throws DataTypeException {
        evn.getEvn2_RecordedDateTime().getTs1_Time().setValue(dateTimeProvider.getCurrentDatetime());
        evn.getEvn5_OperatorID(0).getXcn1_IDNumber().setValue(queueData.getMaindata().getTRANSACTION().getUSERID());

        if(queueData.getQueueDateTime()!=null) {
            evn.getEvn6_EventOccurred().getTs1_Time().setValue(utils.mapDateTimeFormat(queueData.getQueueDateTime()));
        }
    }
}