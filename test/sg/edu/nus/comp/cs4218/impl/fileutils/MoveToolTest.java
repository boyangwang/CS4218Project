package sg.edu.nus.comp.cs4218.impl.fileutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class MoveToolTest {
    IMoveTool moveTool;

    private byte[] fileGetContents(File f) {
        if (f.exists() && f.canRead()) {
            try {
                int read;
                byte[] buf = new byte[4096];
                FileInputStream is = new FileInputStream(f);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((read = is.read(buf)) != -1) {
                    baos.write(buf, 0, read);
                }
                is.close();
                return baos.toByteArray();
            } catch (FileNotFoundException e) {
                System.err.println("unit test error: " + e.toString());
                return null;
            } catch (IOException e) {
                System.err.println("unit test error: " + e.toString());
                return null;
            }
        } else {
            return null;
        }
    }

    private String fileGetString(File f) {
        byte[] result = fileGetContents(f);
        if (result == null) {
            return null;
        } else {
            return new String(result, StandardCharsets.UTF_8);
        }
    }

    @Before
    public void before() {
        moveTool = new MoveTool(new String[0]);
    }

    @After
    public void after() {
        moveTool = null;
    }

    @Test
    public void nullargs() {
        File f = new File("testFile.txt");

        boolean result = moveTool.move(null, null);
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        result = moveTool.move(f, null);
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        result = moveTool.move(null, f);
        assertFalse(f.exists());
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        f.delete();
    }

    @Test
    public void nonExistentSource() {
        File f =  new File("nonExistent");
        File dest = new File("testOut");

        boolean result = moveTool.move(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        dest.delete();
    }

    @Test
    public void unreadableSource() throws IOException {
        File f = new File("badSrc");
        f.createNewFile();
        f.setReadable(false);

        File dest = new File("testDest");
        boolean result = moveTool.move(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }

    @Test
    public void sourceIsDirectory() throws IOException {
        File f = new File("testDir");
        f.mkdir();

        File dest = new File("testDest");
        boolean result = moveTool.move(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }

    @Test
    public void unwritableDest() throws IOException {
        File f = new File("testSrc");
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new FileOutputStream(f));
        pw.write("test string.");
        pw.close();

        File dest = new File("testDest");
        dest.createNewFile();
        dest.setWritable(false);

        boolean result = moveTool.move(f, dest);
        String contents = fileGetString(dest);
        assertEquals(contents, "");
        assertFalse(result);
        assertNotEquals(moveTool.getStatusCode(), 0);

        dest.setWritable(true);
        dest.delete();
        f.delete();
    }

}
