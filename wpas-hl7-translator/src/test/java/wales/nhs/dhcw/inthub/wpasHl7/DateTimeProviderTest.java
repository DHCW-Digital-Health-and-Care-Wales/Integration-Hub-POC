package wales.nhs.dhcw.inthub.wpasHl7;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeProviderTest {

    @Test
    void ProvideDateTimeReturnsStringWithoutSeparatorCharacters() {
        var provider = new DateTimeProvider();

        var result = provider.getCurrentDatetime();

        assertTrue(result.matches("^\\d{14}$"));
    }
}