package wales.nhs.dhcw.inthub.wpasHl7;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {
    public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";

    private DateTimeFormatter dateTimeFormatter;

    public DateTimeProvider() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    }

    public String getCurrentDatetime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
