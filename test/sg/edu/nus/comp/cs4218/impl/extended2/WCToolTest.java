package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.nus.edu.comp.impl.cs4218.WcTool;

public class WCToolTest {

	private WcTool wctool;
	private String testString = "The quick brown fox jumps over the lazy dog.\n";

	@Before
	public void setUp() throws Exception {
		wctool = new WcTool();
	}

	@After
	public void tearDown() throws Exception {
		wctool = null;
	}
	
	@Test 
	public void getCharacterCountTest(){
		String count = wctool.getCharacterCount(testString);
		assertEquals(count,"45");
	}
	
	@Test 
	public void getWordCountTest(){
		String count = wctool.getWordCount(testString);
		assertEquals(count,"9");
	}
	
	@Test 
	public void getNewLineCountTest(){
		String count = wctool.getNewLineCount(testString);
		assertEquals(count,"1");
	}
	
	@Test 
	public void executeCharacterOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " -m "+tempFile.getAbsolutePath());
		assertEquals("45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeWordOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " -w "+tempFile.getAbsolutePath());
		assertEquals("9\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeNewLineOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " -l "+tempFile.getAbsolutePath());
		assertEquals("1\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeTwoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " -l -w "+tempFile.getAbsolutePath());
		assertEquals("1\t9\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeNoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " "+tempFile.getAbsolutePath());
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeFileNameTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " "+tempFile.getName());
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeTwoFilesTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile1T", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile2 = Files.createTempFile("tempFile2T", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		Files.write(tempFile2.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " "+tempFile.getName()+" "+tempFile2.getName());
		assertEquals("1\t9\t45\t"+tempFile.getName()+"\n1\t9\t45\t"+tempFile2.getName(), msg);
		Files.delete(tempFile.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	@Test 
	public void executeTwoFilesWithOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile1T", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile2 = Files.createTempFile("tempFile2T", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		Files.write(tempFile2.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " -l -w "+tempFile.getName()+" "+tempFile2.getName());
		assertEquals("1\t9\t"+tempFile.getName()+"\n1\t9\t"+tempFile2.getName(), msg);
		Files.delete(tempFile.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	@Test 
	public void executeContainsSpaceTest() throws IOException{
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		String msg = wctool.execute(tempWorkingDir, " "+tempFile.getName());
		assertEquals("1\t9\t45\t"+tempFile.getName(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test
    public void getHelpTest() {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
        String helpText = wctool.getHelp();
        assertEquals(helpText, wctool.execute(tempWorkingDir, "-help"));
        assertEquals(helpText, wctool.execute(tempWorkingDir, "-help abcde"));
        assertEquals(helpText, wctool.execute(tempWorkingDir, "-d ., -s -help abcde"));
    }
	
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		String tempName = "tempFile.tmp " + "test";
		String msg = wctool.execute(tempWorkingDir, tempName);
		assertEquals("Error: SOURCE file not found", msg);
		assertTrue(wctool.getStatusCode() != 0);
	}


}