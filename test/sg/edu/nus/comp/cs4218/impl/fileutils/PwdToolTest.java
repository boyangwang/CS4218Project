package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IPwdTool;

public class PwdToolTest {
	private IPwdTool pwdtool; 
	
	@Before
	public void before(){
		pwdtool = new PwdTool(new String[0]);
	}

    @After
	public void after(){
		pwdtool = null;
	}

    /**
     * MUT: getStringForDirectory()
     * Checks that MUT returns system canonical path.
     * @throws IOException
     */
	@Test
	public void getStringForDirectoryTest() throws IOException {
		//Test expected behavior
		//Create a tmp-file and get (existing) parent directory
		String existsDirString = File.createTempFile("exists", "tmp").getParent();
		File existsDir = new File(existsDirString);
		String dirString = pwdtool.getStringForDirectory(existsDir);
		assertTrue(dirString.equals(existsDir.getCanonicalPath()));
		assertEquals(pwdtool.getStatusCode(), 0);
    }


    /**
     * MUT: getStringForDirectory()
     * Program should not crash when attempting to print non-existent directory.
     * @throws IOException
     */
	@Test
	public void getStringForNonExistingDirectoryTest() throws IOException { 
		//Test error-handling 1
		//Reference non-existing file
		File notExistsDir = new File("notexists");
        pwdtool.getStringForDirectory(notExistsDir);
		assertNotEquals(pwdtool.getStatusCode(), 0);
    }

    /**
     * MUT: getStringForDirectory()
     * Program should not crash when null File is supplied.
     * @throws IOException
     */
	@Test
	public void getStringForNullDirectoryTest() throws IOException { 
		//Test error-handling 2
		pwdtool.getStringForDirectory(null);
		assertNotEquals(pwdtool.getStatusCode(), 0);
		
	}

    /**
     * MUT: getStringForDirectory()
     * Directory for "." should be the current working directory.
     * @throws IOException
     */
    @Test
    public void getStringForCurrentDirectoryTest() throws IOException {
        String path = ".";
        String result = pwdtool.getStringForDirectory(new File(path));
        String expected = new File(System.getProperty("user.dir")).getCanonicalPath();
        assertEquals(result, expected);
        assertEquals(pwdtool.getStatusCode(), 0);
    }

    /**
     * MUT: getStringForDirectory()
     * Program should not crash when the supplied directory is a file.
     * @throws IOException
     */
    @Test
    public void getStringForFileTest() throws IOException {
        File f = new File("file.txt");
        pwdtool.getStringForDirectory(f);
        assertNotEquals(pwdtool.getStatusCode(), 0);
        f.delete();
    }

    /**
     * MUT: getStringForDirectory()
     * Program should not crash when an invalid path is supplied.
     * @throws IOException
     */
    @Test
    public void getStringForInvalidPathTest() throws IOException {
        File f = new File("!nvali$d.p}th\\");
        String result = pwdtool.getStringForDirectory(f);
        assertNull(result);
        assertNotEquals(pwdtool.getStatusCode(), 0);
    }
    
    /**
     * MUT: execute()
     * @throws IOException
     */
    @Test
    public void toolExecute() throws IOException {
    	File path = new File(".");
    	PwdTool tool = new PwdTool(new String[0]);
    	String result = tool.execute(path, "");
        String expected = new File(System.getProperty("user.dir")).getCanonicalPath();
    	assertEquals(expected, result);
    	assertEquals(tool.getStatusCode(), 0);
    }

}
