package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ILsTool;

public class LsToolTest {
    private ILsTool lsTool;

    @Before
    public void before() {
        lsTool = new LsTool(new String[0]);
    }

    @After
    public void after() {
        lsTool = null;
    }

    /**
     * MUT: getStringForFiles()
     * Should print file followed by newline.
     */
    @Test
    public void getStringForFilesTest() {
        List<File> files = new ArrayList<>();
        files.add(new File(".test"));
        String expected = String.format("%s\n", ".test");
        String result = lsTool.getStringForFiles(files);
        assertEquals(result, expected);
    }

    /**
     * MUT: getStringForFiles()
     * Should print newline.
     */
    @Test
    public void getStringForNothingTest() {
        List<File> files = new ArrayList<>();
        String expected = "\n";
        String result = lsTool.getStringForFiles(files);
        assertEquals(result, expected);
    }

    /**
     * MUT: getStringForFiles()
     * Program should not crash with null parameter.
     */
    @Test
    public void getStringForNull() {
        String result = lsTool.getStringForFiles(null);
        assertNull(result);
    }

    /**
     * MUT: getFiles()
     * Program should not crash with null parameter.
     */
    @Test
    public void getNull() {
        List<File> result = lsTool.getFiles(null);
        assertNull(result);
    }

    /**
     * MUT: getFiles()
     * Program should not crash when a non-existent file is specified.
     * @throws IOException
     */
    @Test
    public void getNonexistent() throws IOException {
        List<File> result = lsTool.getFiles(new File("nonexistent"));
        assertNull(result);
        assertNotEquals(lsTool.getStatusCode(), 0);
    }

    /**
     * MUT: getFiles()
     * Program should not crash when a non-readable file is specified.
     * @throws IOException
     */
    @Test
    public void getStringForNonReadable() throws IOException {
        File f = new File("testDir");
        f.mkdir();
        f.setReadable(false);
        List<File> result = lsTool.getFiles(f);
        assertNull(result);
        assertNotEquals(lsTool.getStatusCode(), 0);
        f.delete();
    }

    /**
     * MUT: getFiles()
     * Program should not crash when file is specified.
     */
    @Test
    public void cdFile() throws IOException {
        File f = new File("test.txt");
        f.createNewFile();
        List<File> result = lsTool.getFiles(f);
        assertNull(result);
        assertNotEquals(lsTool.getStatusCode(), 0);
        f.delete();
    }
    
    @Test
    public void toolExecuteListFiles() throws IOException {
    	String[] files = new String[]{"a", "b", "c", "d"};
    	StringBuilder expected = new StringBuilder();
    	
    	File dir = new File("testDir");
    	dir.mkdir();
    	for (String name : files) {
    		File f = new File(String.format("%s%s%s", "testDir", File.separator, name));
    		f.createNewFile();
    		expected.append(String.format("%s\n", name));
    	}
    	
    	LsTool tool = new LsTool(new String[]{"testDir"});
    	String result = tool.execute(new File(System.getProperty("user.dir")), "");
    	
    	assertEquals(expected.toString(),result);
    	assertEquals(0, tool.getStatusCode());
    	
    	for (String name : files) {
    		File f = new File(String.format("%s%s%s", "testDir", File.separator, name));
    		f.delete();
    	}
    	dir.delete();
    }
    
    @Test
    public void listDirectoryNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	Method listDir = lsTool.getClass().getDeclaredMethod("listDirectory", new Class[]{File.class});
        listDir.setAccessible(true);
        
        String result = (String) listDir.invoke(lsTool, (Object) null);
        
        assertNull(result);
    }
    
    @Test
    public void executeNoArgumentsCwd() throws IOException {
    	String[] files = new String[]{"a", "b", "c", "d"};
    	StringBuilder expected = new StringBuilder();
    	
    	File dir = new File("testDir");
    	dir.mkdir();
    	for (String name : files) {
    		File f = new File(String.format("%s%s%s", "testDir", File.separator, name));
    		f.createNewFile();
    		expected.append(String.format("%s\n", name));
    	}
    	
    	LsTool tool = new LsTool(new String[0]);
    	String result = tool.execute(dir, "");
    	
    	assertEquals(expected.toString(),result);
    	assertEquals(0, tool.getStatusCode());
    	
    	for (String name : files) {
    		File f = new File(String.format("%s%s%s", "testDir", File.separator, name));
    		f.delete();
    	}
    	dir.delete();
    }
}
