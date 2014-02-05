package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICatTool;

public class CatToolTest {
    private ICatTool catTool;

    @Before
    public void before() {
        catTool = new CatTool(new String[0]);
    }

    @After
    public void after() {
        catTool = null;
    }

    /**
     * MUT: getStringForFile()
     * Program should not crash when trying to read a directory.
     * @throws IOException
     */
    @Test
    public void getStringForDirectoryTest() throws IOException {
        File f = new File("testDir");
        f.mkdir();
        String result = catTool.getStringForFile(f);
        assertNull(result);
        assertNotEquals(catTool.getStatusCode(), 0);
        f.delete();
    }

    /**
     * MUT: getStringForFile()
     * Program should not crash when a null argument is supplied.
     * @throws IOException
     */
    @Test
    public void getStringForNullTest() throws IOException {
        String result = catTool.getStringForFile(null);
        assertNull(result);
        assertNotEquals(catTool.getStatusCode(), 0);
    }

    /**
     * MUT: getStringForFile()
     * Program should not crash when a non-existent file is specified.
     * @throws IOException
     */
    @Test
    public void getStringForNonexistentTest() throws IOException {
        String result = catTool.getStringForFile(new File("nonexistent"));
        assertNull(result);
        assertNotEquals(catTool.getStatusCode(), 0);
    }

    /**
     * MUT: getStringForFile()
     * Program should not crash when a non-readable file is specified.
     * @throws IOException
     */
    @Test
    public void getStringForNonReadable() throws IOException {
        File f = new File("test.txt");
        f.setReadable(false);
        String result = catTool.getStringForFile(f);
        assertNull(result);
        assertNotEquals(catTool.getStatusCode(), 0);
        f.delete();
    }

    // TODO: Various malformed files. (Remove when swapping)
    // 1. File that contains \x00 (null)
    // 2. File that contains \x04 (Ctrl + D)
    // 3. File that contains \x03 (Ctrl + C)
    // 4. File that contains \x1a (Ctrl + Z)
    // 5. ASCII file.
    // 6. UTF-8 file.
    // 7. UTF-8 file with invalid codepoints.
}
