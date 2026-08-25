package au.gov.nehta.common.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class ArgumentUtilsTest {

    @Test
    public void isNullOrBlank_treatsNullEmptyAndWhitespaceAsBlank() {
        assertTrue(ArgumentUtils.isNullOrBlank(null));
        assertTrue(ArgumentUtils.isNullOrBlank(""));
        assertTrue(ArgumentUtils.isNullOrBlank("   "));
        assertFalse(ArgumentUtils.isNullOrBlank("a"));
    }

    @Test
    public void isNullOrEmpty_treatsNullAndEmptyCollection() {
        assertTrue(ArgumentUtils.isNullOrEmpty(null));
        assertTrue(ArgumentUtils.isNullOrEmpty(Collections.emptyList()));
        assertFalse(ArgumentUtils.isNullOrEmpty(List.of("x")));
    }

    @Test
    public void isEqual_matchesObjectsEqualsSemantics() {
        assertTrue(ArgumentUtils.isEqual(null, null));
        assertFalse(ArgumentUtils.isEqual(null, "a"));
        assertTrue(ArgumentUtils.isEqual("a", "a"));
        assertFalse(ArgumentUtils.isEqual("a", "b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNotNullNorBlank_rejectsWhitespaceOnly() {
        ArgumentUtils.checkNotNullNorBlank("  ", "name");
    }

    @Test
    public void checkMaxLength_allowsNullAndWithinLimit() {
        ArgumentUtils.checkMaxLength(null, 1, "name");
        ArgumentUtils.checkMaxLength("ab", 2, "name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMaxLength_rejectsTooLong() {
        ArgumentUtils.checkMaxLength("abc", 2, "name");
    }

    @Test
    public void checkNullOrBlank_allowsNullAndBlank() {
        ArgumentUtils.checkNullOrBlank(null, "name");
        ArgumentUtils.checkNullOrBlank("", "name");
        ArgumentUtils.checkNullOrBlank("  ", "name");
    }

    @Test
    public void checkNotNullNorEmpty_acceptsNonEmpty() {
        ArgumentUtils.checkNotNullNorEmpty(List.of("x"), "items");
        assertEquals(1, List.of("x").size());
    }
}
