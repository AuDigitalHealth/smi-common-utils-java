package au.gov.nehta.common.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UriUtilsTest {

    @Test
    public void randomUri_usesUuidUrn() {
        String uri = UriUtils.randomUri();
        assertTrue(uri.startsWith("urn:uuid:"));
        assertTrue(uri.length() > "urn:uuid:".length());
    }
}
