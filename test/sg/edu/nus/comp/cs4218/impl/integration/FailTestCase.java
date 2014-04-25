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
	 * BUG_ID #4
	 * 
	 * reading from user input does not work in pipe tool
	 * 
	 *  Shell.java:201
	 */
	@Test
	public void testStdinInPipe() {

		String ctrld = "123456789\n987654321\nctrl-d";
		byte[] b = ctrld.getBytes();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(b);
		sh = new Shell(is, os);
		String cmd = "paste - | cut -c1-5 -";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
		try {
			String out = new String(os.toByteArray(), "UTF-8");
			assertTrue(out.contains("12345\n98765\n"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}



	}

	/**
	 * BUG_ID #16
	 * 
	 * Throw exception on tool thread, unable to catch here as the thread is create 
	 * and execute inside method execute() in shell
	 * 
	 *  PasteTool.java:178
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

	/*
	 * BUG_ID #13
	 */
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

	/*
	 * BUG_ID #3
	 */
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

	/*
	 * BUG_ID #18
	 */
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

	/*
	 * BUG_ID #21
	 */
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

	/*
	 * BUG_ID #7
	 */
	@Test
	public void testGrepWithDouble() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sh = new Shell(System.in, os);
		String cmd = "grep -A 1.2 normalFile";
		ITool result = sh.parse(cmd);
		sh.execute(result);
		hold();
	}

	/*
	 * BUG_ID #15
	 */
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

	/*
	 * BUG_ID #11
	 */
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
}
