package wales.nhs.dhcw.inthub.wpasHl7;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_YMD_FORMAT = "yyyyMMdd";
    public static final String DATETIME_WITH_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dateyyyyMMddFormatter;
    private final DateTimeFormatter dateTimeFormatterWithTFormat;

    public DateTimeProvider() {

        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.dateyyyyMMddFormatter = DateTimeFormatter.ofPattern(DATE_YMD_FORMAT);
        this.dateTimeFormatterWithTFormat = DateTimeFormatter.ofPattern(DATETIME_WITH_T_FORMAT);

    }

    public String getCurrentDatetime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public DateTimeFormatter getDateFormatter(){
        return dateFormatter;
    }

    public DateTimeFormatter getDateyyyyMMddFormatter() {
        return dateyyyyMMddFormatter;
    }

    public DateTimeFormatter getDateTimeFormatterWithTFormat() {
        return dateTimeFormatterWithTFormat;
    }
}
