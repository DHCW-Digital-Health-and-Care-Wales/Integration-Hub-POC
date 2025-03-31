package wales.nhs.dhcw.inthub.wpasHl7.mapping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WpasMapUtilsTest {

    private final WpasMapUtils wpasMapUtils = new WpasMapUtils();

    @Test
    void mapDateFormat() {
        var wpasDate = "2025-03-31";
        var hlDate = "20250331";

        var result = wpasMapUtils.mapDateFormat(wpasDate);

        assertEquals(hlDate, result);
    }

    @Test
    void mapDateTimeFormat() {
        var wpasTimestamp = "2025-02-10T11:24:48";
        var hlTimestamp = "20250210112448";

        var result = wpasMapUtils.mapDateTimeFormat(wpasTimestamp);

        assertEquals(hlTimestamp, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\"\""})
    void notNullNorBlankDetectEmptyValues(String text) {
        assertFalse(wpasMapUtils.notNullNorBlank(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {".", "'", "John"})
    void notNullNorBlankDetectNonEmptyValues(String text) {
        assertTrue(wpasMapUtils.notNullNorBlank(text));
    }

    @Test
    void stripEmptyDoubleQuotes() {
        var text = "\"\"";

        var result = wpasMapUtils.stripEmptyDoubleQuotes(text);

        assertEquals("", result);
    }
}