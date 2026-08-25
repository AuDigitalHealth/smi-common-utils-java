/*
 * Copyright 2009 NEHTA
 *
 * Licensed under the NEHTA Open Source (Apache) License; you may not use this file except in compliance with the
 * License. A copy of the License is in the 'LICENSE.txt' file, which should be provided with this work.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package au.gov.nehta.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Static class used to manage access to System properties.
 */
public final class PropertyUtils {

    /*
     * Text that will be accepted as a 'true' boolean value.
     */
    private static final List<String> TRUE_VALUES = List.of("true", "yes", "on", "1");

    /**
     * Gets the value of the system property with the given name, throwing an
     * exception if the system property is not set.
     *
     * @param name Name of the system property. Cannot be null nor a blank string.
     * @return The value of the system property.
     * @throws IllegalArgumentException if not found.
     */
    public static String getSystemProperty(String name) {
        assert (name != null) : "'name' is null.";
        assert (!name.trim().isEmpty()) : "'name' is a blank string.";

        String value = System.getProperty(name);
        if (value == null) {
            throw new IllegalArgumentException("System property '" + name
                    + "' was not set.");
        }
        return value;
    }

    /**
     * Gets the value of the system property with the given name, returning a
     * default value if the system property is not set.
     *
     * @param name         Name of the system property. Cannot be null nor a blank string.
     * @param defaultValue Default value.
     * @return If found, the value of the system property, else the default value.
     */
    public static String getSystemProperty(String name, String defaultValue) {
        assert (name != null) : "'name' is null.";
        assert (!name.trim().isEmpty()) : "'name' is a blank string.";

        return System.getProperty(name, defaultValue);
    }

    /**
     * Sets a system property with a given name.
     *
     * @param name  Name of the system property. Cannot be null nor a blank string.
     * @param value Value of the system property. Cannot be null nor a blank string.
     */
    public static void setSystemProperty(String name, String value) {
        assert (name != null) : "'name' is null.";
        assert (!name.trim().isEmpty()) : "'name' is a blank string.";
        assert (value != null) : "'value' is null.";

        System.setProperty(name, value);
    }

    /**
     * Load the properties from the given file (UTF-8).
     *
     * @param file Path to properties file.
     * @return The properties loaded from the properties file.
     * @throws FileNotFoundException Thrown when the properties files doesn't exist.
     * @throws IOException           Thrown when IO error occurred reading in the properties.
     */
    public static Properties loadProperties(File file)
            throws FileNotFoundException, IOException {
        assert (file != null) : "'file' is null.";

        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            return loadProperties(reader);
        }
    }

    /**
     * Load the properties from a character stream.
     *
     * @param reader Character stream containing the properties values. Cannot be null.
     * @return The properties loaded.
     * @throws IOException Thrown when IO error occurred reading in the properties.
     */
    public static Properties loadProperties(Reader reader) throws IOException {
        assert (reader != null) : "'reader' is null.";

        Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

    /**
     * Load the properties from a byte stream.
     *
     * @param inputStream Byte stream containing the properties values. Cannot be null.
     * @return The properties loaded.
     * @throws IOException Thrown when IO error occurred reading in the properties.
     */
    public static Properties loadProperties(InputStream inputStream)
            throws IOException {
        assert (inputStream != null) : "'inputStream' is null.";

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    /**
     * Retrieves a property value as a boolean. The string property values that is
     * returned as a true boolean value are: "true", "yes", "on" and "1".
     * Comparisons are case insensitive.
     *
     * @param properties   Properties object to inspect. Cannot be null.
     * @param name         Name of the property. Cannot be null nor a blank string.
     * @param defaultValue Boolean value to return if the property is not set.
     * @return boolean property value.
     */
    public static boolean getBooleanProperty(Properties properties, String name,
                                             boolean defaultValue) {
        assert (properties != null) : "'properties' is null.";
        assert (name != null) : "'name' is null.";
        assert (!name.trim().isEmpty()) : "'name' is a blank string.";

        // Retrieve property value from properties object
        String propertyValue = properties.getProperty(name);

        // Return default value if property value isn't set.
        if (ArgumentUtils.isNullOrBlank(propertyValue)) {
            return defaultValue;
        }

        // Remove leading and trailing whitespaces and convert to lower case
        propertyValue = propertyValue.trim().toLowerCase(Locale.ROOT);

        // Check if the property value matches any of the acceptable 'true' values
        return TRUE_VALUES.contains(propertyValue);
    }

    /*
     * Private constructor - static class.
     */
    private PropertyUtils() {
    }
}
