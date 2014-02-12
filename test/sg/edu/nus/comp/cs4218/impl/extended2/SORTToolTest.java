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
import sg.nus.edu.comp.impl.cs4218.SortTool;

public class SORTToolTest {
	
	private ISortTool sorttool;
	@Before
	public void setUp() throws Exception {
		sorttool = new SortTool();
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
		assertEquals(result, "test\ntesT\nteST\ntEST\nTest\nTEst\nTESt\nTEST\n");
	}
	
	@Test
	public void sortFileTestDuplicateTest(){
		String input = "test\nTEST\ntest\napple\nApple\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "apple\nApple\ntest\ntest\nTEST\n");
	}
	
	@Test
	public void sortFileTestSpecialCharacterTest(){
		String input = "test\nTEST\n!!!\n@.@\n死\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "!!!\n@.@\ntest\nTEST\n死\n");
	}
	
	@Test
	public void sortFileTestBlankLinesTest(){
		String input = "test\n\n\nTEST\n";
		String result = sorttool.sortFile(input);
		assertEquals(result, "\n\ntest\nTEST\n");
	}

	@Test
	public void sortFileTestSortedAlreadyTest(){
		String input = "test\nTEST";
		String result = sorttool.sortFile(input);
		assertEquals(result, "test\nTEST\n");
	}
	
	@Test
	public void checkIfSortedTestSortedAlreadyTest() {
		String input = "test\nTEST";
		String result = sorttool.checkIfSorted(input);
		assertEquals(result, "");
	}
	
	@Test
	public void checkIfSortedTestUnsortedTest() {
		String input = "TEST\ntest\nTEST";
		String result = sorttool.checkIfSorted(input);
		assertEquals(result, "1: disorder:TEST\n");
	}

	@Test
	public void executeTestOneFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.execute(file.getParentFile(), " " + file.getName());
		assertEquals(result, "apple\nhello world\nHello world\n");
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String result = sorttool.execute(file.getParentFile(), " " + file.getName());
		assertEquals(result, "\n");
		Files.delete(file.toPath());
	}

	@Test
	public void executeTestMultipleSameFileTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "hello world\nHello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.execute(file.getParentFile(), " " + file.getName() + " " + file.getName() + " " + file.getName());
		assertEquals(result, "apple\napple\napple\nhello world\nhello world\nhello world\nHello world\nHello world\nHello world\n");
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
		
		String result = sorttool.execute(file1.getParentFile(), " " + directory.getName() + "\\" + file3.getName() +
				" " + file2.getAbsolutePath() + " " + file1.getName());		
		assertEquals(result, "apple\nApple\nApple\nbanana\nBanana\nhello world\nHello world\norange\nOrange\n");
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
		String result = sorttool.execute(file.getParentFile(), " " + file.getName() + " invalidfile.tmp");
		assertEquals(result, "Error: FILE is not found");
		assertEquals(sorttool.getStatusCode(),1);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestOneFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nhello world\nHello world\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.execute(file.getParentFile(), " -c " + file.getName());
		assertEquals(result, "");
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestMultipleFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String input = "apple\nHello world\nhello world\napple\n";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		String result = sorttool.execute(file.getParentFile(), " -c " + file.getName());
		String expected = "sort: " + file.getName() + " 2: disorder:Hello world\n";
		assertEquals(result, expected);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyFileCheckIfSortedTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String result = sorttool.execute(file.getParentFile(), " -c " + file.getName());
		assertEquals(result, "");
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestInvalidFileCheckIfSortedTest() {
		String result = sorttool.execute(new File(System.getProperty("user.home")), " -c invalidfile.tmp");
		assertEquals(result, "Error: FILE is not found");
		assertEquals(sorttool.getStatusCode(),1);
	}
	
	@Test
	public void executeTestInvalidOptionTest() throws IOException {
		File file = File.createTempFile("tempFile", ".tmp");
		String result = sorttool.execute(file.getParentFile(), " -c -d " + file.getName());
		String expected = "Error: comm: unrecognized option '-d'\nTry 'comm -help' for more information.\n";
		assertEquals(result, expected);
		assertEquals(sorttool.getStatusCode(),1);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeTestEmptyArgumentTest() {
		String result = sorttool.execute(new File(System.getProperty("user.home")), " ");
		assertEquals(result, "Error: Missing parameter for OPTION FILE");
		assertEquals(sorttool.getStatusCode(),1);
	}
	
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		String result = sorttool.execute(dir.getParentFile(), dir.getName());
		assertEquals(result,"Error: FILE 01 is not a file");
		assertEquals(sorttool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
}
