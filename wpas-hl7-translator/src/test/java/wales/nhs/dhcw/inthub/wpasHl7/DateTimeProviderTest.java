package wales.nhs.dhcw.inthub.wpasHl7;

import org.junit.jupiter.api.Test;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Hl7DateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeProviderTest {

    @Test
    void ProvideDateTimeReturnsStringWithoutSeparatorCharacters() {
        Hl7DateFormat hl7DateFormat = new Hl7DateFormat();

        var provider = new DateTimeProvider(hl7DateFormat.getDateTimeFormatter());

        var result = provider.getCurrentDatetime();

        assertTrue(result.matches("^\\d{14}$"));
    }
}