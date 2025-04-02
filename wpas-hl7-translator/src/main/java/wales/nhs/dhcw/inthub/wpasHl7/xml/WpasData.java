package wales.nhs.dhcw.inthub.wpasHl7.xml;


import java.util.Date;

public class WpasData {
    private MAINDATA maindata;
    private Date queueDateTime;

    public WpasData(MAINDATA maindata, Date queueDateTime) {
        this.maindata = maindata;
        this.queueDateTime = queueDateTime;
    }
    public WpasData() {}

    public MAINDATA getMaindata() {
        return maindata;
    }

    public void setMaindata(MAINDATA maindata) {
        this.maindata = maindata;
    }

    public Date getQueueDateTime() {
        return queueDateTime;
    }

    public void setQueueDateTime(Date queueDateTime) {
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
