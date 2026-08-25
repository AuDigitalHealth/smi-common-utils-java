package au.gov.nehta.common.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Singleton class that formats information in a standard and consistent way.
 */
public final class StandardFormatter {

    private static final StandardFormatter INSTANCE = new StandardFormatter();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ROOT);

    private static final String UNKNOWN_DATE = "<UnknownDate>";

    private static final String UNKNOWN_CLASS = "<UnknownClass>";

    private static final String UNKNOWN_METHOD = "<UnknownMethod>";

    /**
     * Returns an instance of StandardFormatter.
     *
     * @return StandardFormatter instance.
     */
    public static StandardFormatter getInstance() {
        return INSTANCE;
    }

    private StandardFormatter() {
    }

    /**
     * Formats a date-time, e.g. 2008-12-30 12:45:00.000+1000.
     *
     * @param timeInMillis time in milliseconds
     * @return formatted string representing date-time
     */
    public String formatDateTime(long timeInMillis) {
        return formatDateTime(new Date(timeInMillis));
    }

    /**
     * Formats a date-time, e.g. 2008-12-30 12:45:00.000+1000.
     *
     * @param date date object
     * @return formatted string representing date-time or {@code <UnknownDate>} if
     * date is null
     */
    public String formatDateTime(Date date) {
        if (date == null) {
            return UNKNOWN_DATE;
        }
        return DATE_TIME_FORMATTER.format(
                Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));
    }

    /**
     * Formats a code location.
     *
     * @param className  class name
     * @param methodName method name
     * @return formatted string representing the code location
     */
    public String formatLocation(String className, String methodName) {
        String classNameStr = ArgumentUtils.isNullOrBlank(className)
                ? UNKNOWN_CLASS
                : className;
        String methodNameStr = ArgumentUtils.isNullOrBlank(methodName)
                ? UNKNOWN_METHOD
                : methodName;
        return String.format("%s.%s()", classNameStr, methodNameStr);
    }
}
