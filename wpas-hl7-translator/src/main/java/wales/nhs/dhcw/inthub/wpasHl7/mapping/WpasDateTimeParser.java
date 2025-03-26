package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class WpasDateTimeParser {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATETIME_FORMAT_WITHOUTSPACE = "yyyyMMddHHmmss";

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter dateyyyyMMddFormatter;

    public WpasDateTimeParser() {
        this.dateyyyyMMddFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT_WITHOUTSPACE);
        this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    }

    public LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, dateFormatter);
    }

    public LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }
    public String formaterDateTime(OffsetDateTime dateTime) {
        return  dateTime.format(dateyyyyMMddFormatter);
    }
}
