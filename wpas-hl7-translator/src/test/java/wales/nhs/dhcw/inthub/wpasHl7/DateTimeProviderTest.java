package wales.nhs.dhcw.inthub.wpasHl7;

import org.junit.jupiter.api.Test;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.hl7.Hl7DateFormatProvider;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeProviderTest {

    @Test
    void ProvideDateTimeReturnsStringWithoutSeparatorCharacters() {
        var provider = new DateTimeProvider(Hl7DateFormatProvider.getDateTimeFormatter());

        var result = provider.getCurrentDatetime();

        assertTrue(result.matches("^\\d{14}$"));
    }
}