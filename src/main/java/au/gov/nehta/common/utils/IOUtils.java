package au.gov.nehta.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Utility class for common I/O functions.
 *
 * <p>
 * File and stream text I/O uses UTF-8.
 */
public final class IOUtils {

    /**
     * Reads the content of a file into a string (UTF-8).
     *
     * @param inputFile File to read from. Cannot be null.
     * @return Text content from the file.
     * @throws IOException Thrown when the file cannot be found, or there are I/O
     *                     errors
     *                     reading from the file.
     */
    public static String read(File inputFile) throws IOException {
        assert (inputFile != null) : "'inputFile' is null.";

        return Files.readString(inputFile.toPath(), StandardCharsets.UTF_8);
    }

    /**
     * Reads the content of a byte stream into a string (UTF-8). The byte stream
     * will be
     * closed after the contents are read.
     *
     * @param inputStream Byte stream to read from. Cannot be null.
     * @return Text content from the byte stream.
     * @throws IOException Thrown when there are I/O errors reading from the byte
     *                     stream.
     */
    public static String read(InputStream inputStream) throws IOException {
        assert (inputStream != null) : "'inputStream' is null.";

        return read(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Reads the content of a character stream into a string. The character stream
     * will be closed after the contents are read.
     *
     * @param reader Character stream to read from. Cannot be null.
     * @return Text content from the character stream.
     * @throws IOException Thrown when there are I/O errors reading from the
     *                     character
     *                     stream.
     */
    public static String read(Reader reader) throws IOException {
        assert (reader != null) : "'reader' is null.";

        try (Reader owned = reader; StringWriter sw = new StringWriter()) {
            owned.transferTo(sw);
            return sw.toString();
        }
    }

    /**
     * Writes text content to a file (UTF-8).
     *
     * @param outputFile File to write to. Cannot be null.
     * @param contents   Text content to write to the file. Cannot be null nor a
     *                   blank
     *                   string.
     * @throws IOException Thrown when there are I/O errors writing to the file.
     */
    public static void write(File outputFile, String contents) throws IOException {
        assert (outputFile != null) : "'outputFile' is null.";
        assert (contents != null) : "'contents' is null.";
        assert (!contents.trim().isEmpty()) : "'contents' is a blank string.";

        Files.writeString(outputFile.toPath(), contents, StandardCharsets.UTF_8);
    }

    /**
     * Writes text content to a byte stream (UTF-8).
     *
     * @param outputStream Byte stream to write to. Cannot be null.
     * @param contents     Text content to write to the byte stream. Cannot be null
     *                     nor
     *                     a blank string.
     * @throws IOException Thrown when there are I/O errors writing to the byte
     *                     stream.
     */
    public static void write(OutputStream outputStream, String contents)
            throws IOException {
        assert (outputStream != null) : "'outputStream' is null.";
        assert (contents != null) : "'contents' is null.";
        assert (!contents.trim().isEmpty()) : "'contents' is a blank string.";

        write(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), contents);
    }

    /**
     * Writes text content to a character stream. The stream is closed after
     * writing.
     *
     * @param writer   Character stream to write to. Cannot be null.
     * @param contents Text content to write to the character stream. Cannot be null
     *                 nor
     *                 a blank string.
     * @throws IOException Thrown when there are I/O errors writing to the character
     *                     stream.
     */
    public static void write(Writer writer, String contents) throws IOException {
        assert (writer != null) : "'writer' is null.";
        assert (contents != null) : "'contents' is null.";
        assert (!contents.trim().isEmpty()) : "'contents' is a blank string.";

        try (Writer owned = writer) {
            owned.write(contents);
            owned.flush();
        }
    }

    /*
     * Private constructor to prevent instantiation of a utility class.
     */
    private IOUtils() {
    }

}
