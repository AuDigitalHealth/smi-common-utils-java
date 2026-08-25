package au.gov.nehta.common.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IOUtilsTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void readWrite_fileRoundTripUsesUtf8() throws Exception {
        File file = temp.newFile("sample.txt");
        String contents = "cafe \u00e9";
        IOUtils.write(file, contents);
        assertEquals(contents, IOUtils.read(file));
        assertEquals(contents, Files.readString(file.toPath(), StandardCharsets.UTF_8));
    }

    @Test
    public void read_inputStreamUsesUtf8AndCloses() throws Exception {
        byte[] bytes = "hello \u20ac".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        assertEquals("hello \u20ac", IOUtils.read(in));
        assertEquals(-1, in.read());
    }

    @Test
    public void write_outputStreamUsesUtf8() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.write(out, "ping");
        assertEquals("ping", out.toString(StandardCharsets.UTF_8));
    }
}
