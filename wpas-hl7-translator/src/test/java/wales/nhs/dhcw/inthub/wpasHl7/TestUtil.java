package wales.nhs.dhcw.inthub.wpasHl7;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

public final class TestUtil {
    private TestUtil() {
    }

    static Reader getTestFileReader(String path) {
        return new InputStreamReader(getTestFileStream(path));
    }

    static InputStream getTestFileStream(String path) {
        return TestUtil.class.getClassLoader().getResourceAsStream(path);
    }

    static String getTestFileContent(String path) {
        return new BufferedReader(getTestFileReader(path))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}