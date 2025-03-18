package wales.nhs.dhcw.inthub.wpasHl7;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_YMD_FORMAT = "yyyyMMdd";

    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter dateyyyyMMddFormatter;

    public DateTimeProvider() {

        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        this.dateyyyyMMddFormatter = DateTimeFormatter.ofPattern(DATE_YMD_FORMAT);
    }

    public String getCurrentDatetime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public DateTimeFormatter getDateyyyyMMddFormatter() {
        return dateyyyyMMddFormatter;
    }
}
