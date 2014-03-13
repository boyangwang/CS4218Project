package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;

public class COMMToolTest {

	private ICommTool commtool;
	
	@Before
	public void setUp() throws Exception {
		commtool = new CommTool();
	}

	@After
	public void tearDown() throws Exception {
		commtool = null;
	}

	@Test
	public void testGetHelp() {
		String result = commtool.getHelp();
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("FILE1\tName of the file 1\nFILE2\tName of the file 2\n\n"));
		assertTrue(result.contains("no options, produce three-column output. Col"));
	}
	
	@Test
	public void compareFileEqualListTest() {
		String input1 = "hello world";
		String input2 = "hello world";
		String expected = "\t\t\t\thello world\n";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesUnequalListTest() {
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\ncomm: file 2 is not in sorted order\n\t\tfinal\nthis is a test\ncomm: file 1 is not in sorted order\nfile\nabc\n";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesCheckSortStatusEqualListTest() {
		String input1 = "hello world";
		String input2 = "hello world";
		String expected = "\t\t\t\thello world\n";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}
	
	@Test
	public void compareFilesCheckSortStatusUnequalListTest() {
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\ncomm: file 2 is not in sorted order\n";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesDoNotCheckSortStatusEqualListTest() {
		String input1 = "hello world";
		String input2 = "hello world";
		String expected = "\t\t\t\thello world\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}
	
	@Test
	public void compareFilesDoNotCheckSortStatusUnequalListTest() {
		String input1 = "hello world\nthis is a test\nfile\nabc";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\t\t\thello world\n\t\tfinal\nthis is a test\nfile\nabc\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesOneEmptyListTest() {
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n\t\tfinal\n";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesCheckSortStatusOneEmptyListTest() {
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\ncomm: file 2 is not in sorted order\n";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesDoNotCheckSortStatusOneEmptyListTest() {
		String input1 = "";
		String input2 = "baby\nhello world\nfinal";
		String expected = "\t\tbaby\n\t\thello world\n\t\tfinal\n";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}
	
	@Test
	public void compareFilesTwoEmptyListTest() {
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFiles(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesCheckSortStatusTwoEmptyListTest() {
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFilesCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}

	@Test
	public void compareFilesDoNotCheckSortStatusTwoEmptyListTest() {
		String input1 = "";
		String input2 = "";
		String expected = "";
		String result = commtool.compareFilesDoNotCheckSortStatus(input1, input2);
		assertEquals(expected.compareTo(result),0);
	}
	
	@Test
	public void executeNoOptionTest() throws IOException {
		//Test expected behavior
		//Create tmp folder with 2 files. Execute comm with no option
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\ncomm: file 1 is not in sorted order\nfile\n";
		String result = commtool.execute(tempFolder, " "+ file1.getName() + " " + file2.getName());
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	@Test
	public void executeOneOptionTest() throws IOException {
		//Test expected behavior
		//Create tmp folder with 2 files. Execute comm with a option
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\nfile\n";
		String result = commtool.execute(tempFolder, "-d " + file1.getName() + " " + file2.getName());
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	@Test
	public void executeTwoOptionTest() throws IOException {
		//Test expected behavior
		//Create tmp folder with 2 files. Execute comm with two option
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "\t\tbaby\n\t\t\t\thello world\nthis is a test\ncomm: file 1 is not in sorted order\n";
		String result = commtool.execute(tempFolder, "-d -c " + file1.getName() + " " + file2.getName());
		assertEquals(temp.compareTo(result), 0);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}

	@Test
	public void executeOptionHelpTest() {
		//Test expected behavior
		//only have help in the option, no file names
		String result = commtool.execute(new File(System.getProperty("java.io.tmpdir")), "-help");
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line"));
		assertTrue(result.endsWith("Brief information about supported options\n"));
		assertTrue(result.contains("no options, produce three-column output."));
		assertTrue(result.contains("file 1\nFILE2\tName of the file 2"));
	}
	
	@Test
	public void executeOptionsIncludeHelpTest() throws IOException {
		//Test expected behavior
		//have multiple options including help.
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(tempFolder, "-d -help -c" + file1.getName() + " " + file2.getName());
		assertTrue(result.startsWith("NAME\n\ncomm - compare two sorted files line by line"));
		assertTrue(result.endsWith("Brief information about supported options\n"));
		assertTrue(result.contains("no options, produce three-column output."));
		assertTrue(result.contains("file 1\nFILE2\tName of the file 2"));	
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}

	@Test
	public void executeExmptyFileTest() throws IOException {
		//Test expected behavior
		//One of the file does not contains any string
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(tempFolder, "-d -c " + file1.getName() + " " + file2.getName());
		temp = "hello world\nthis is a test\ncomm: file 1 is not in sorted order\n";
		assertTrue(result.equals(temp));
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	@Test
	public void executeInvalidOptionTest() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(tempFolder, "-d -i -c " + file1.getName() + " " + file2.getName());
		temp = "Error: comm: unrecognized option '-i'\nTry 'comm -help' for more information.\n";
		assertEquals(commtool.getStatusCode(),1);
		assertTrue(result.equals(temp));
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	@Test
	public void executeInvalidFileNameTest() throws IOException {
		//Error-handling
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File file1 = Files.createFile(new File(tempFolder, "-r tempFile1.txt").toPath()).toFile();
		File file2 = Files.createFile(new File(tempFolder, "tempFile2.txt").toPath()).toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file1.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		temp = "baby\nhello world";
		Files.write(file2.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(tempFolder, "-d -c " + file1.getName() + " " + file2.getName());
		temp = "Error: FILE 1 is not found";
		assertTrue(result.equals(temp));
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(file1.toPath());
		Files.delete(file2.toPath());
		Files.delete(tempFolder.toPath());
	}
	
	@Test
	public void executeSameFileNoOptionTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(file.getParentFile(), file.getName() + " " + file.getName());
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\n\t\t\t\tfile\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeSameFileNoCheckTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(file.getParentFile(), "-d " + file.getName() + " " + file.getName());
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\n\t\t\t\tfile\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeSameFileCheckTest() throws IOException{
		File file = Files.createTempFile("temp File", ".tmp").toFile();
		String temp = "hello world\nthis is a test\nfile";
		Files.write(file.toPath(), temp.getBytes(), StandardOpenOption.CREATE);
		String result = commtool.execute(file.getParentFile(), "-c " + file.getName() + " " + file.getName());
		String expected = "\t\t\t\thello world\n\t\t\t\tthis is a test\ncomm: file 1 is not in sorted order\n";
		assertEquals(result,expected);
		Files.delete(file.toPath());
	}
	
	@Test
	public void executeFolderTest() throws IOException{
		File dir = Files.createTempDirectory("temp dir").toFile();
		String result = commtool.execute(dir.getParentFile(), "-c " + dir.getName() + " " + dir.getName());
		assertEquals(result,"Error: FILE 1 is not a file");
		assertEquals(commtool.getStatusCode(),1);
		Files.delete(dir.toPath());
	}
}