package au.gov.nehta.common.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import org.junit.Test;

public class PropertyUtilsTest {

    @Test
    public void getBooleanProperty_acceptsConfiguredTrueTokensCaseInsensitively() {
        Properties properties = new Properties();
        properties.setProperty("a", "TRUE");
        properties.setProperty("b", "Yes");
        properties.setProperty("c", "on");
        properties.setProperty("d", "1");
        properties.setProperty("e", "no");
        assertTrue(PropertyUtils.getBooleanProperty(properties, "a", false));
        assertTrue(PropertyUtils.getBooleanProperty(properties, "b", false));
        assertTrue(PropertyUtils.getBooleanProperty(properties, "c", false));
        assertTrue(PropertyUtils.getBooleanProperty(properties, "d", false));
        assertFalse(PropertyUtils.getBooleanProperty(properties, "e", true));
        assertTrue(PropertyUtils.getBooleanProperty(properties, "missing", true));
    }
}
