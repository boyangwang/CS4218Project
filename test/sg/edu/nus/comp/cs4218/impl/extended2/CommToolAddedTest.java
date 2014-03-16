/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.CommandParser;

public class CommToolAddedTest {
	ICommTool ct;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

    private File file;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHelp() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.execute(null, "");
		assertEquals("comm : Compares two sorted files line by line. With no options, produce three-column output. Column one\n"
				+ "contains lines unique to FILE1, column two contains lines unique to FILE2, and column three contains lines\n"
				+ "common to both files.\n\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2\n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable\n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n", result);
	}
	
	@Test
	public void testReadFile() throws NoSuchMethodException, SecurityException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		final File temp = folder.newFile("file.txt");
		Files.write(temp.toPath(), "abc".getBytes(), StandardOpenOption.APPEND);
		Method readFile = CommTool.class.getDeclaredMethod("readContentsOfFile", File.class);
		readFile.setAccessible(true);
		String result = (String) readFile.invoke(ct, temp);
		assertEquals("abc", result);
	}
	@Test
	public void testReadFileEmpty() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		final File temp = folder.newFile("empty.txt");
		Method readFile = CommTool.class.getDeclaredMethod("readContentsOfFile", File.class);
		readFile.setAccessible(true);
		String result = (String) readFile.invoke(ct, temp);
		assertEquals("", result);
	}
	@Test
	public void testReadFileNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		final File temp = null;
		Method readFile = CommTool.class.getDeclaredMethod("readContentsOfFile", File.class);
		readFile.setAccessible(true);
		try {
			String result = (String) readFile.invoke(ct, temp);
			assertTrue(false);
		}
		catch (InvocationTargetException cause) {
			try {
				throw cause.getCause();
			}
			catch (NullPointerException e) {
				assertTrue(true);
			}
			catch (Throwable e) {
				assertTrue(false);
			}
		}
		catch (Throwable e) {
			assertTrue(false);
		}
	}
	@Test
	public void testReadFileDirectory() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		final File temp = folder.newFolder("folder");
		Method readFile = CommTool.class.getDeclaredMethod("readContentsOfFile", File.class);
		readFile.setAccessible(true);
		try {
			String result = (String) readFile.invoke(ct, temp);
			assertTrue(false);
		}
		catch (InvocationTargetException cause) {
			try {
				throw cause.getCause();
			}
			catch (FileNotFoundException e) {
				assertTrue(true);
			}
			catch (Throwable e) {
				assertTrue(false);
			}
		}
		catch (Throwable e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testCompareFilesNormal() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFiles("4\nd\nb\n2\n", "3\nc\na\n1\n");
		String expected = "\t\t3\n4\n\t\tc\ncomm: file 2 is not in sorted order\n"
				+ "\t\ta\n\t\t1\nd\ncomm: file 1 is not in sorted order\nb\n2\n";
		assertEquals(expected, result);
	}
	@Test
	public void testCompareFilesNocheckNormal() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFilesDoNotCheckSortStatus("4\nd\n", "3\nc\n");
		String expected = "\t\t3\n4\n\t\tc\nd\n";
		assertEquals(expected, result);
	}
	@Test
	public void testCompareFilesCheckNormal() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFilesCheckSortStatus("4\nd\n", "3\nc\n");
		String expected = "\t\t3\n4\n\t\tc\nd\n";
		assertEquals(expected, result);
	}
	@Test
	public void testCompareFilesNullPointer() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFilesCheckSortStatus("4\nd\n", null);
		assertEquals("Internal NullPointerError.\n", result);
	}
	@Test
	public void testCompareFilesCheckNullPointer() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFilesCheckSortStatus("4\nd\n", null);
		assertEquals("Internal NullPointerError.\n", result);
	}
	@Test
	public void testCompareFilesNocheckNullPointer() {
		ct = (ICommTool)CommandParser.parse("comm -help", null);
		String result = ct.compareFilesCheckSortStatus("4\nd\n", null);
		assertEquals("Internal NullPointerError.\n", result);
	}
}
