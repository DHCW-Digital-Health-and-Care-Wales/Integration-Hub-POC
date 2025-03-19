package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Hl7DateFormat;

import java.time.LocalDate;

public class MapUtils {

    private Hl7DateFormat hl7DateFormat;
    private WpasDateTimeParser wpasDateTimeParser;

    public MapUtils() {
        hl7DateFormat = new Hl7DateFormat();
        wpasDateTimeParser = new WpasDateTimeParser();
    }

    public String mapDateFormat(String dateString) {
        var cleanedDateString = stripEmptyDoubleQuotes(dateString);
        if (notNullNorBlank(cleanedDateString)) {
            LocalDate date = wpasDateTimeParser.parseDate(cleanedDateString);
            return date.format(hl7DateFormat.getDateFormatter());
        } else {
            return dateString;
        }
    }

    public String mapDateTimeFormat(String dateTimeString) {
        if (notNullNorBlank(dateTimeString)) {
            var date = wpasDateTimeParser.parseDateTime(dateTimeString);
            return date.format(hl7DateFormat.getDateTimeFormatter());
        } else {
            return dateTimeString;
        }
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


    public String readPatientGivenName(String forename) {
        if (notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(0, spacePosition);
            } else {
                return forename;
            }
        }
        return "";
    }

    public String readPatientMiddleNames(String forename) {
        if (notNullNorBlank(forename)) {
            var spacePosition = forename.indexOf(' ');
            if (spacePosition >= 0) {
                return forename.substring(spacePosition);
            }
        }
        return "";
    }
}