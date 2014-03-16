package sg.edu.nus.comp.cs4218.impl.fileutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.*;

public class CopyToolTest {
    private final static String TEST_DEST = "testDest";

    ICopyTool copyTool;

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

    private boolean compareByteArrays(byte[] buf1, byte[] buf2) {
        if (buf1 == null && buf2 == null) {
            return true;
        } else if (buf1 == null || buf2 == null) {
            return false;
        } else if (buf1.length != buf2.length) {
            return false;
        } else {
            for (int i = 0; i < buf1.length; i++) {
                if (buf1[i] != buf2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    @Before
    public void before() {
        copyTool = new CopyTool(new String[0]);
    }

    @After
    public void after() {
        copyTool = null;
    }

    /**
     * MUT: copy()
     * Program should not crash under any combination of null arguments.
     */
    @Test
    public void nullArgs() {
        File f = new File("testFile.txt");

        boolean result = copyTool.copy(null, null);
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        result = copyTool.copy(f, null);
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        result = copyTool.copy(null, f);
        assertFalse(f.exists());
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        f.delete();
    }

    /**
     * Program should not crash when non-existent source is specified.
     */
    @Test
    public void nonExistentSource() {
        File f =  new File("nonExistent");
        File dest = new File("testOut");

        boolean result = copyTool.copy(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        dest.delete();
    }

    /**
     * Program should not crash for an unreadable source file.
     * @throws IOException
     */
    @Test
    public void unreadableSource() throws IOException {
        File f = new File("badSrc");
        f.createNewFile();
        f.setReadable(false);

        File dest = new File(TEST_DEST);
        boolean result = copyTool.copy(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }

    /**
     * Copy does not support directories.
     * Program should not crash, and directory should not be copied.
     * @throws IOException
     */
    @Test
    public void sourceIsDirectory() throws IOException {
        File f = new File("testDir");
        f.mkdir();

        File dest = new File(TEST_DEST);
        boolean result = copyTool.copy(f, dest);
        assertFalse(dest.exists());
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }

    /**
     * MUT: copy()
     * Copying empty file should result in destination being created.
     */
    @Test
    public void destCreated() throws IOException {
        File f = new File("testFile");
        f.createNewFile();

        File dest = new File("nonExistent");
        copyTool.copy(f, dest);

        boolean result = dest.exists();
        assertTrue(result);
        assertEquals(copyTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }
    
    @Test
    public void testCleanup() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	File f = new File("testFile");
        f.createNewFile();
        
        Method cleanup = copyTool.getClass().getDeclaredMethod("cleanup", new Class[]{File.class});
        cleanup.setAccessible(true);
        cleanup.invoke(copyTool, f);
        
        assertFalse(f.exists());
    }
    
    @Test
    public void toolExecute() throws IOException {
    	File f = new File("testFile");
        f.createNewFile();
        
    	CopyTool tool = new CopyTool(new String[]{"testFile", "nonExistent"});
    	String result = tool.execute(new File(System.getProperty("user.dir")), "");
    	
    	assertEquals("", result);
    	assertEquals(0, tool.getStatusCode());
    	
    	File dest = new File("nonExistent");
    	dest.delete();
    }
    
    @Test
    public void toolExecuteInvalidArgs() {
    	CopyTool tool = new CopyTool(new String[]{"testFile"});
    	String result = tool.execute(new File(System.getProperty("user.dir")), "");
    	
    	assertEquals("Invalid arguments.", result);
    	assertNotEquals(0, tool.getStatusCode());
    }

    /**
     * MUT: copy()
     * Copying a file should be lossless.
     * @throws IOException
     */
    @Test
    public void destContentsIdentical() throws IOException {
        Random rand = new Random();
        File f = new File("testSrc");
        f.createNewFile();

        byte[] ref = new byte[131072];
        FileOutputStream fos = new FileOutputStream(f);
        for (int i = 0; i < 131072; i++) {
            byte b = (byte)rand.nextInt(255);
            fos.write(b);
            ref[i] = b;
        }
        fos.close();

        File dest = new File(TEST_DEST);
        copyTool.copy(f, dest);
        byte[] contents = fileGetContents(dest);
        assertTrue(compareByteArrays(ref, contents));
        assertEquals(copyTool.getStatusCode(), 0);

        f.delete();
        dest.delete();
    }

    /**
     * MUT: copy()
     * Program should not crash if destination file is unwritable.
     * @throws IOException
     */
    @Test
    public void unwritableDest() throws IOException {
        File f = new File("testSrc");
        f.createNewFile();

        PrintWriter pw = new PrintWriter(new FileOutputStream(f));
        pw.write("test string.");
        pw.close();

        File dest = new File(TEST_DEST);
        dest.createNewFile();
        dest.setWritable(false);

        boolean result = copyTool.copy(f, dest);
        String contents = fileGetString(dest);
        assertEquals(contents, "");
        assertFalse(result);
        assertNotEquals(copyTool.getStatusCode(), 0);

        dest.setWritable(true);
        dest.delete();
        f.delete();
    }
}
