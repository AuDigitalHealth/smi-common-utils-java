package au.gov.nehta.common.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

public class StandardFormatterTest {

    @Test
    public void formatDateTime_nullReturnsUnknown() {
        assertEquals("<UnknownDate>", StandardFormatter.getInstance().formatDateTime((Date) null));
    }

    @Test
    public void formatDateTime_formatsKnownPattern() {
        String formatted = StandardFormatter.getInstance().formatDateTime(0L);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{4}"));
    }

    @Test
    public void formatLocation_substitutesUnknowns() {
        assertEquals("<UnknownClass>.<UnknownMethod>()",
                StandardFormatter.getInstance().formatLocation(null, "  "));
        assertEquals("au.Example.run()",
                StandardFormatter.getInstance().formatLocation("au.Example", "run"));
    }
}
