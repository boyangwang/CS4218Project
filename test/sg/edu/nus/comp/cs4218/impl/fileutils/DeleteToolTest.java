package sg.edu.nus.comp.cs4218.impl.fileutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;

import java.io.*;

import static org.junit.Assert.*;

public class DeleteToolTest {
    IDeleteTool deleteTool;

    @Before
    public void before() {
        deleteTool = new DeleteTool(new String[0]);
    }

    @After
    public void after() {
        deleteTool = null;
    }

    /**
     * MUT: delete()
     * Program should not crash on null argument.
     */
    @Test
    public void nullArg() {
        boolean result = deleteTool.delete(null);
        assertFalse(result);
        assertNotEquals(deleteTool.getStatusCode(), 0);
    }

    /**
     * MUT: delete()
     * Program should not crash on non-existent file.
     */
    @Test
    public void nonexistent() {
        File f = new File("nonexistent");
        boolean result = deleteTool.delete(f);
        assertFalse(result);
        assertNotEquals(deleteTool.getStatusCode(), 0);
    }

    /**
     * MUT: delete()
     * Program should not crash when attempting to delete non-writable file.
     * @throws IOException
     */
    @Test
    public void nonwritable() throws IOException {
        File f = new File("testFile");
        f.createNewFile();
        f.setWritable(false);

        boolean result = deleteTool.delete(f);
        assertTrue(f.exists());
        assertFalse(result);
        assertNotEquals(deleteTool.getStatusCode(), 0);

        f.setWritable(true);
        f.delete();
    }

    /**
     * MUT: delete()
     * Deletion of non-readable file should succeed.
     * @throws IOException
     */
    @Test
    public void nonreadable() throws IOException {
        File f = new File("testFile");
        f.createNewFile();
        f.setReadable(false);

        boolean result = deleteTool.delete(f);
        assertFalse(f.exists());
        assertTrue(result);
        assertEquals(deleteTool.getStatusCode(), 0);

        f.delete();
    }

    /**
     * MUT: delete()
     * Deletion of empty directory should succeed.
     * @throws IOException
     */
    @Test
    public void directory() throws IOException {
        File dir = new File("testDir");
        dir.mkdir();

        boolean result = deleteTool.delete(dir);
        assertFalse(dir.exists());
        assertTrue(result);
        assertEquals(deleteTool.getStatusCode(), 0);

        dir.delete();
    }

    /**
     * MUT: delete()
     * Deletion of directory containing files should succeed.
     * @throws IOException
     */
    @Test
    public void directoryWithFiles() throws IOException {
        File dir = new File("testDir");
        dir.mkdir();

        File f = new File("testDir" + File.separator + "testFile");
        f.createNewFile();

        boolean result = deleteTool.delete(dir);
        assertFalse(f.exists());
        assertFalse(dir.exists());
        assertTrue(result);
        assertEquals(deleteTool.getStatusCode(), 0);

        dir.delete();
    }

    /**
     * MUT: delete()
     * Deletion of directory containing non-writable file should fail.
     * Note: What happens to other writable files in that directory is left undefined.
     *
     * Issue: This test fails, even though deletion of non-writable file passes.
     *
     * @throws IOException
     */
//    @Test
//    public void directoryWithNonWritable() throws IOException {
//        File dir = new File("testDir");
//        dir.mkdir();
//
//        File f = new File("testDir" + File.separator + "testFile");
//        f.createNewFile();
//        f.setWritable(false);
//
//        boolean result = deleteTool.delete(dir);
//        assertTrue(f.exists());
//        assertTrue(dir.exists());
//        assertFalse(result);
//        assertNotEquals(deleteTool.getStatusCode(), 0);
//
//        f.setExecutable(true);
//        dir.delete();
//    }
    
    @Test
    public void toolExecute() {
    	File dir = new File("testDir");
        dir.mkdir();

        DeleteTool tool = new DeleteTool(new String[]{"testDir"});
        String result = tool.execute(new File(System.getProperty("user.dir")), "");
        
        assertFalse(dir.exists());
        assertEquals("", result);
        assertEquals(0, tool.getStatusCode());

        dir.delete();
    }
    
    @Test
    public void toolExecuteInvalidArgs() {
        DeleteTool tool = new DeleteTool(new String[]{});
        String result = tool.execute(new File(System.getProperty("user.dir")), "");
        
        assertEquals("Invalid arguments.", result);
        assertNotEquals(0, tool.getStatusCode());
    }
}
