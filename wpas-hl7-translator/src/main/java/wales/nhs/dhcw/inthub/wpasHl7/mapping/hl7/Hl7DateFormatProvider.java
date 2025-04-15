package wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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

    public static SimpleDateFormat getSimpleDateTimeFormatter() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/London")));
        return simpleDateFormat;
    }

    public static String getFormatDateTime(String dateTime) {
        try {
            SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-ddHHmm");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
            return simpleDateFormat.format(requiredFormat.parse(dateTime));
        } catch (Exception e) {
            return "";
        }
    }
}
