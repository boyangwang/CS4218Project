package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
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
    
    @Test
    public void getCorrectString() throws IOException {
    	String expected = "hello world!";
    	FileWriter fw = new FileWriter("catTest.txt", false);
    	fw.write(expected);
    	fw.close();
    	
    	File f = new File("catTest.txt");
    	String result = catTool.getStringForFile(f);
    	assertEquals(expected, result);
    	assertEquals(0, catTool.getStatusCode());
    	
    	f.delete();
    }
    
    @Test
    public void executeCatStdin() {
    	String expected = "This is stdin!";
    	
    	String result = catTool.execute(new File(System.getProperty("user.dir")), expected);
    	
    	assertEquals(expected, result);
    	assertEquals(0, catTool.getStatusCode());
    }
    
    @Test
    public void executeCorrectString() throws IOException {
    	String expected = "hello world!";
    	FileWriter fw = new FileWriter("catTest.txt", false);
    	fw.write(expected);
    	fw.close();
    	
    	catTool = new CatTool(new String[]{"catTest.txt"});
    	File f = new File("catTest.txt");
    	String result = catTool.execute(new File(System.getProperty("user.dir")), "");
    	
    	assertEquals(expected, result);
    	assertEquals(0, catTool.getStatusCode());
    	
    	f.delete();
    }
}
