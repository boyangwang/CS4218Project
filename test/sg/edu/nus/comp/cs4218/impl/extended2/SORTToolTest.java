package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;

public class SORTToolTest {
	
	private ISortTool sorttool;
	@Before
	public void setUp() throws Exception {
		sorttool = new SortTool(null);
	}

	@After
	public void tearDown() throws Exception {
		sorttool = null;
	}
	
	@Test
	public void testGetHelpTest() {
		String result = sorttool.getHelp();
		assertTrue(result.startsWith("NAME\n\nsort - sort lines of text files\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("DESCRIPTION\n\nWrite sorted concatenation of all FILE(s)"));
		assertTrue(result.contains("-c\tCheck whether the given file is already sorted, if it"));
	}

	@Test
	public void sortFileTestArrangementTest() {
		String input = "test\nTest\nTEst\nTESt\nTEST\ntEST\nteST\ntesT\n";
		String result = sorttool.sortFile(input);
		assertEquals("TEST\nTESt\nTEst\nTest\ntEST\nteST\ntesT\ntest\n", result);
	}
	
	@Test
	public void sortFileTestDuplicateTest(){
		String input = "test\nTEST\ntest\napple\nApple\n";
		String result = sorttool.sortFile(input);
		assertEquals("Apple\nTEST\napple\ntest\ntest\n", result);
	}
	
	@Test
	public void sortFileTestSpecialCharacterTest(){
		String input = "test\nTEST\n!!!\n@.@\n���\n";
		String result = sorttool.sortFile(input);
		assertEquals("!!!\n@.@\nTEST\ntest\n���\n", result);
	}
	
	@Test
	public void sortFileTestBlankLinesTest(){
		String input = "test\n\n\nTEST\n";
		String result = sorttool.sortFile(input);
		assertEquals("\n\nTEST\ntest\n", result);
	}

	@Test
	public void sortFileTestSortedAlreadyTest(){
		String input = "test\nTEST";
		String result = sorttool.sortFile(input);
		assertEquals("TEST\ntest\n", result);
	}
	
	@Test
	public void checkIfSortedTestSortedAlreadyTest() {
		String input = "TEST\ntest";
		String result = sorttool.checkIfSorted(input);
		assertEquals("", result);
	}
	
	@Test
	public void checkIfSortedTestUnsortedTest() {
		String input = "TEST\ntest\nTEST";
		String result = sorttool.checkIfSorted(input);
		assertEquals("3: disorder:TEST\n", result);
	}

	@Test
	public void executeTestOneFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals("Hello world\napple\nhello world\n", result);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals("", result);
		Files.delete(file.toPath());
	}

	@Test
	public void executeTestMultipleSameFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getName(), file.getName(), file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals("Hello world\nHello world\nHello world\napple\napple\napple\nhello world\nhello world\nhello world\n", result);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestMultipleDiffFileTest() throws IOException {
		File file1 = File.createTempFile("tempFile 1", ".tmp");
		File directory = new File(System.getProperty("user.home"));
		File file2 = Files.createFile(new File(directory, "tempFile 2.tmp").toPath()).toFile();
		directory = Files.createTempDirectory("temp Folder").toFile();
		File file3 = Files.createFile(new File(directory, "tempFile 3.tmp").toPath()).toFile();
		
		String input = "hello world\nHello world\napple\n";
		Files.write(file1.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "orange\nOrange\nApple";
		Files.write(file2.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		input = "banana\nBanana\nApple";
		Files.write(file3.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		
		sorttool = new SortTool(new String[]{directory.getName() + System.getProperty("file.separator") + file3.getName(), file2.getAbsolutePath(), file1.getName()});
		String result = sorttool.execute(file1.getParentFile(), null);		
		assertEquals("Apple\nApple\nBanana\nHello world\nOrange\napple\nbanana\nhello world\norange\n", result);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(file3.toPath());
		Files.delete(directory.toPath());
	}
	
	@Test
	public void executeTestMultipleFileWithOneInvalidFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{file.getName(), "invalidfile.tmp"});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(String.format("Error: FILE is not found%n"), result);
		assertEquals(1, sorttool.getStatusCode());
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestOneFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nhello world\nHello world\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c", file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals(String.format("sort: " + file.getName() + " 3: disorder:Hello world%n"), result);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestMultipleFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nHello world\nhello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		sorttool = new SortTool(new String[]{"-c", file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = "sort: " + file.getName() + " 2: disorder:Hello world\n";
		assertEquals(expected, result);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{"-c", file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		assertEquals("", result);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestInvalidFileCheckIfSortedTest() {
		sorttool = new SortTool(new String[]{"-c", "invalidfile.tmp"});
		String result = sorttool.execute(new File(System.getProperty("user.home")), null);
		assertEquals(String.format("Error: FILE is not found%n"), result);
		assertEquals(1, sorttool.getStatusCode());
	}
	
	@Test
	public void executeTestInvalidOptionTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		sorttool = new SortTool(new String[]{"-c", "-d", file.getName()});
		String result = sorttool.execute(file.getParentFile(), null);
		String expected = String.format("Error: sort: unrecognized option '-d'%nTry 'sort -help' for more information.%n");
		assertEquals(expected, result);
		assertEquals(1, sorttool.getStatusCode());
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyArgumentTest() {
		sorttool = new SortTool(null);
		String result = sorttool.execute(new File(System.getProperty("user.home")), null);
		assertEquals(String.format("Error: Missing parameter for OPTION FILE%n"), result);
		assertEquals(1, sorttool.getStatusCode());
	}
	
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		sorttool = new SortTool(new String[]{dir.getName()});
		String result = sorttool.execute(dir.getParentFile(), null);
		assertEquals(String.format("Error: FILE 01 is not a file%n"), result);
		assertEquals(1, sorttool.getStatusCode());
		Files.delete(dir.toPath());
	}
}
