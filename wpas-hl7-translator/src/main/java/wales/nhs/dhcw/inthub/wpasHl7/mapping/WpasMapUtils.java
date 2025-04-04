package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Hl7DateFormatProvider;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class WpasMapUtils {

    private WpasDateTimeParser wpasDateTimeParser;

    public WpasMapUtils() {
        wpasDateTimeParser = new WpasDateTimeParser();
    }

    public String mapDateFormat(String dateString) {
        var cleanedDateString = stripEmptyDoubleQuotes(dateString);
        if (notNullNorBlank(cleanedDateString)) {
            LocalDate date = wpasDateTimeParser.parseDate(cleanedDateString);
            return date.format(Hl7DateFormatProvider.getDateFormatter());
        } else {
            return dateString;
        }
    }

    public String mapDateTimeFormat(String dateTimeString) {
        if (notNullNorBlank(dateTimeString)) {
            var date = wpasDateTimeParser.parseDateTime(dateTimeString);
            return date.format(Hl7DateFormatProvider.getDateTimeFormatter());
        } else {
            return dateTimeString;
        }
    }
    public String mapDateTimeFormat(OffsetDateTime dateTime) {
        return  wpasDateTimeParser.formaterDateTime(dateTime);
    }

    public boolean notNullNorBlank(String string) {
        return null != string && !stripEmptyDoubleQuotes(string).isBlank();
    }

    public String stripEmptyDoubleQuotes(String input) {
        if (input != null && input.equals("\"\"")) {
            return "";
        } else {
            return input;
        }
    }
}