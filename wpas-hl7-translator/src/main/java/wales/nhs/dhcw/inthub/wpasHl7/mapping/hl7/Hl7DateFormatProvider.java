package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import java.time.format.DateTimeFormatter;

import static wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants.DATETIME_FORMAT;
import static wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants.DATE_FORMAT;

public class Hl7DateFormatProvider {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private Hl7DateFormatProvider() {
    }


    public static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public static DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }
}
