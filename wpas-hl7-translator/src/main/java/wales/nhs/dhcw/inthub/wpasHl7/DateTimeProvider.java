package wales.nhs.dhcw.inthub.wpasHl7;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {

    private final DateTimeFormatter dateTimeFormatter;

    public DateTimeProvider(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getCurrentDatetime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
}
