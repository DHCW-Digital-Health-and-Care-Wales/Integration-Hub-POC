package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import java.time.format.DateTimeFormatter;

import static wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants.DATETIME_FORMAT;
import static wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants.DATE_FORMAT;

public class Hl7DateFormat {
    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter dateFormatter;

    public Hl7DateFormat() {

        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    }


    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }
}
