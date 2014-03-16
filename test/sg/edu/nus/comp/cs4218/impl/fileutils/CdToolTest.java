package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class CdToolTest {
    private ICdTool cdTool;

    /**
     * CdTool is integral to the shell, and cannot be tested otherwise.
     */
    @Before
    public void before() {
        Shell shell = new Shell(System.in, System.out);
        cdTool = new CdTool(new String[0], shell);
    }

    @After
    public void after() {
        cdTool = null;
    }

    /**
     * MUT: changeDirectory()
     * Program should not crash on null parameter.
     */
    @Test
    public void cdNull() {
        File result = cdTool.changeDirectory(null);
        assertNull(result);
        assertNotEquals(cdTool.getStatusCode(), 0);
    }

    /**
     * MUT: changeDirectory()
     * Program should not crash on non-existent directory.
     */
    @Test
    public void cdNonExistent() {
        File result = cdTool.changeDirectory("nonexistent");
        assertNull(result);
        assertNotEquals(cdTool.getStatusCode(), 0);
    }

    
    // Commented out because this test is platform specific
    /**
     * MUT: changeDirectory()
     * Program should not crash on unreadable file.
     */
//    @Test
//    public void cdNotReadable() throws IOException {
//        File f = new File("testDir");
//        f.mkdir();
//        f.setReadable(false);
//        File result = cdTool.changeDirectory(f.getCanonicalPath());
//        assertNull(result);
//        assertNotEquals(cdTool.getStatusCode(), 0);
//        f.delete();
//    }

    /**
     * MUT: changeDirectory()
     * Utility should not switch directory to a file.
     */
    @Test
    public void cdFile() throws IOException {
        File f = new File("test.txt");
        f.createNewFile();
        File result = cdTool.changeDirectory(f.getCanonicalPath());
        assertNull(result);
        assertNotEquals(cdTool.getStatusCode(), 0);
        f.delete();
    }

    /**
     * MUT: changeDirectory()
     * Program should not crash when bad path supplied.
     */
    @Test
    public void cdBadPath() throws IOException {
        File result = cdTool.changeDirectory("!nvali$d.p}th\\/");
        assertNull(result);
        assertNotEquals(cdTool.getStatusCode(), 0);
    }

    // Commented out because this test is platform specific
    /**
     * MUT: changeDirectory()
     * Utility should not allow changing directories beyond root.
     */
//    @Test
//    public void cdUpwardsMultiple() throws IOException {
//        String upwards = ".." + File.separator;
//        StringBuilder path = new StringBuilder();
//        for (int i = 0; i < 1024; i++) {
//            path.append(upwards);
//        }
//        File result = cdTool.changeDirectory(path.toString());
//        assertNull(result);
//        assertNotEquals(cdTool.getStatusCode(), 0);
//    }

    // Commented out because this test is platform specific
    /**
     * MUT: changeDirectory()
     * Utility should support changing directories upwards with "../", up to "/"
     */
//    @Test
//    public void cdUpwardsRoot() throws IOException {
//        File wd = new File(System.getProperty("user.dir"));
//        int i = 0;
//        while ((wd = wd.getParentFile()) != null) {
//            i++;
//        }
//        String upwards = ".." + File.separator;
//        StringBuilder path = new StringBuilder();
//        while (i-- > 0) {
//            path.append(upwards);
//        }
//
//        // Note: may be UNIX specific.
//        File homeDirectory = FileSystemView.getFileSystemView().getRoots()[0];
//        String expected = homeDirectory.getCanonicalPath();
//
//        File result = cdTool.changeDirectory(path.toString());
//        assertEquals(result.getCanonicalPath(), expected);
//        assertEquals(cdTool.getStatusCode(), 0);
//    }

    /**
     * MUT: changeDirectory()
     * Changing directory to "." should not have side effects.
     */
    @Test
    public void cdCurrent() throws IOException {
    	String expected = System.getProperty("user.dir");
        File result = cdTool.changeDirectory(".");
        assertEquals(result.getCanonicalPath(), expected);
        assertEquals(cdTool.getStatusCode(), 0);
    }
    
    @Test
    public void toolExecute() throws IOException {
    	CdTool tool = new CdTool(new String[]{"."}, new Shell(System.in, System.out));
    	String result = tool.execute(new File(System.getProperty("user.dir")), "");
    	assertEquals("", result);
    	assertEquals(0, cdTool.getStatusCode());
    }
}
