package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WCToolTest {

	private WcTool wctool;
	private String testString = "The quick brown fox jumps over the lazy dog.\n";

	@Before
	public void setUp() throws Exception {
		wctool = new WcTool(null);
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
		wctool = new WcTool(new String[]{"-m", tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("45\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeWordOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-w", tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("9\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeNewLineOptionTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-l", tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeTwoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{"-l", "-w", tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeNoOptionsTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getAbsolutePath()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeFileNameTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test 
	public void executeTwoFilesTest() throws IOException{
		File tempFile = Files.createTempFile("tempFile1T", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		File tempFile2 = Files.createTempFile("tempFile2T", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		Files.write(tempFile2.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getName(), tempFile2.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName()+"\n1\t9\t45\t"+tempFile2.getName()+System.lineSeparator(), msg);
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
		wctool = new WcTool(new String[]{"-l", "-w", tempFile.getName(), tempFile2.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t"+tempFile.getName()+"\n1\t9\t"+tempFile2.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
		Files.delete(tempFile2.toPath());
	}
	
	@Test 
	public void executeContainsSpaceTest() throws IOException{
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		wctool = new WcTool(new String[]{tempFile.getName()});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals("1\t9\t45\t"+tempFile.getName()+System.lineSeparator(), msg);
		Files.delete(tempFile.toPath());
	}
	
	@Test
    public void getHelpTest() {
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
        String helpText = wctool.getHelp();
        assertEquals(helpText, new WcTool(new String[]{"-help"}).execute(tempWorkingDir, null));
        assertEquals(helpText, new WcTool(new String[]{"-help", "abcde"}).execute(tempWorkingDir, null));
        assertEquals(String.format("wc: illegal option -- d%n"), new WcTool(new String[]{"-d", ".,", "-s", "-help", "abcde"}).execute(tempWorkingDir, null));
    }
	
	@Test
	public void executeInvalidSourceNameTest() throws IOException {
		File tempFile = Files.createTempFile("temp File", ".tmp").toFile();
		Files.write(tempFile.toPath(), testString.getBytes(),  StandardOpenOption.CREATE);
		File tempWorkingDir = new File(System.getProperty("java.io.tmpdir"));
		String tempName = "tempFile.tmp " + "test";
		wctool = new WcTool(new String[]{tempName});
		String msg = wctool.execute(tempWorkingDir, null);
		assertEquals(String.format("Error: SOURCE file not found%n"), msg);
		assertTrue(wctool.getStatusCode() != 0);
	}


}