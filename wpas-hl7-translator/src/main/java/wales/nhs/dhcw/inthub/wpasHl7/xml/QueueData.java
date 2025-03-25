package wales.nhs.dhcw.inthub.wpasHl7.xml;


import java.time.OffsetDateTime;

public class QueueData {
    private MAINDATA maindata;
    private OffsetDateTime queueDateTime;

    public QueueData(MAINDATA maindata, OffsetDateTime queueDateTime) {
        this.maindata = maindata;
        this.queueDateTime = queueDateTime;
    }
    public QueueData() {}

    public MAINDATA getMaindata() {
        return maindata;
    }

    public void setMaindata(MAINDATA maindata) {
        this.maindata = maindata;
    }

    public OffsetDateTime getQueueDateTime() {
        return queueDateTime;
    }

    public void setQueueDateTime(OffsetDateTime queueDateTime) {
        this.queueDateTime = queueDateTime;
    }

    @Override
    public String toString() {
        return "QueueData{" +
                "maindata=" + maindata +
                ", queueDateTime=" + queueDateTime +
                '}';
    }
}
