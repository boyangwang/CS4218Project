package sg.edu.nus.comp.cs4218.impl.integration;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class FailTestCase {
	Shell sh;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		sh = null;
	}

	@BeforeClass
	public static void initialize() throws IOException {

		File myFile1 = new File("emptyFile");
		myFile1.createNewFile();

		File myFile2 = new File("fileName has space");
		myFile2.createNewFile();
		writeFile("fileName has space", "content1\nconten2");

		File myFile3 = new File("normalFile");
		myFile3.createNewFile();
		writeFile("normalFile", "content1\ncontent2");

		File myFile4 = new File("copyFile");
		myFile4.createNewFile();
		writeFile("copyFile", "content1\ncontent2");
	}

	@AfterClass
	public static void method() {

		File file1 = new File("emptyFile.txt");
		if (file1.exists()) {
			file1.delete();
		}

		File file2 = new File("fileName has space.txt");
		if (file2.exists()) {
			file2.delete();
		}

		File file3 = new File("normalFile");
		if (file3.exists()) {
			file3.delete();
		}

		File file4 = new File("copyFile");
		if (file4.exists()) {
			file4.delete();
		}
	}

	public static void writeFile(String fileName, String s) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(s);
		out.close();

	}

	public static void hold() {

		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e) {
			//do nth
		}
	}

	/**
	 * Throw exception on tool thread, unable to catch here as the thread is create 
	 * and execute inside method execute() in shell 
	 */
	@Test
	public void testPasteEmptyFile() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "paste emptyFile";
		ITool result = sh.parse(cmd);
		hold();
		try {
			sh.execute(result);
			String out = new String(os.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPasteFileWithSpaceInName() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "paste fileName has space";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			File file = new File("fileName has space");
			String content = new String(Files.readAllBytes(file.toPath()));
			String out = new String(os.toByteArray(), "UTF-8");
			//assertEquals(content,out);
			assertTrue(out.contains(content));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCtrlZWhenNoToolRunning() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "ctrl-z";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			assertTrue(out.length() > 0); // shd have prompt user for next command but nothing in the output
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCtrlZToStopRunningTool() {
		String ctrlz = "ctrl-z \nctrl-z\nctrl-d";
		byte[] b = ctrlz.getBytes();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(b);
		sh = new Shell(is, os);
		String cmd = "paste - ";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		String out;
		try {
			out = new String(os.toByteArray(), "UTF-8");
			assertEquals("", out); // ctrl-z doesn't terminate the program, instead it is treated as part of the input
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCutHelp() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "cut -help ";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			assertEquals("Command Format - cut [OPTIONS] [FILE]\n"
					+ "*		FILE - Name of the file, when no file is present (denoted by \"-\") use standard input OPTIONS\n"
					+ "*			-c LIST: Use LIST as the list of characters to cut out. Items within the list may be\n"
					+ "*					separated by commas, and ranges of characters can be separated with dashes.\n"
					+ "*					For example, list <-5,10,12,18-30> 1 through 5, 10,12 and\n"
					+ "*					18 through 30.\n"
					+ "*			-d DELIM: Use DELIM as the field-separator character instead of the TAB character\n"
					+ "*			-help : Brief information about supported options;\n", out); //no meaningful help message
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUniqWithStdin() {
		String ctrlz = "line \nline\nctrl-d";
		byte[] b = ctrlz.getBytes();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(b);
		sh = new Shell(is, os);
		String cmd = "uniq -";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			assertTrue(out.contains("line")); // shd have prompt user for next command but nothing in the output
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPipeWithStdin() {
		String ctrlz = "line \nline\nctrl-d";
		byte[] b = ctrlz.getBytes();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(b);
		sh = new Shell(is, os);
		String cmd = "paste - | cut -c 1-5 -";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			assertTrue(out.contains("line \nline")); // no user input was taken in
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPasteWithDelim() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "paste -d \"\\t\" normalFile normalFile normalFile ";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			String expected = "content1\tcontetn1\tcontent1\ncontent2\tcontetn2\tcontent2";
			assertEquals(expected, out.substring(0, expected.length()));// no user input was taken in
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCutWithDelim() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// cut 
		sh = new Shell(System.in, os);
		String cmd = "cut -d \"\\t\" -f 1-4 normalFile ";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			String expected = "content1\ncontent2";
			assertEquals(expected, out.substring(0, expected.length()));// no user input was taken in
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCopyToSameFile() throws IOException {
		File file = new File("copyFile");
		String contentBefore = new String(Files.readAllBytes(file.toPath()));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "copy copyFile copyFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			file = new File("copyFile");
			String contentAfter = new String(Files.readAllBytes(file.toPath()));
			assertEquals(contentBefore, contentAfter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGrepWithDouble() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "grep -A 1.2 normalFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
	}

	@Test
	public void testGrepWithWiredPattern() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "grep -1 normalFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String output = new String(os.toByteArray(), "UTF-8");
			assertEquals("", output);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCommInvalidOption() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "comm -e normalFile emptyFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String output = new String(os.toByteArray(), "UTF-8");
			assertTrue(output.contains("-e")); // wrong option should be -e instead
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGrepHelp() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "grep -help";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String output = new String(os.toByteArray(), "UTF-8");
			assertFalse(output.contains("Illegal argument"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCommMissingFile() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "comm -c normalFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String output = new String(os.toByteArray(), "UTF-8");
			assertFalse(output.contains("java.lang.NullPointerException"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCDPipeOtherTool() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "cd testDir | pwd";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String output = new String(os.toByteArray(), "UTF-8");
			output = output.split("\n")[0];
			assertTrue(output.contains("testDir"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCDWithInvalidArgument() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "ls";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String lsBefore = new String(os.toByteArray(), "UTF-8");
			os.reset();
			cmd = "cd ...";
			result = sh.parse(cmd);
			sh.execute(result);
			hold();

			cmd = "ls";
			result = sh.parse(cmd);
			sh.execute(result);
			hold();

			String lsAfter = new String(os.toByteArray(), "UTF-8");

			assertEquals(lsBefore, lsAfter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
