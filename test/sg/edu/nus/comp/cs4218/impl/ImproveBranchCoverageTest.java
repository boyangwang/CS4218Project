package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;
import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.extended2.IWcTool;
import sg.edu.nus.comp.cs4218.fileutils.ICatTool;
import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;
import sg.edu.nus.comp.cs4218.impl.extended2.SortTool;
import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;
import sg.edu.nus.comp.cs4218.impl.extended2.WcTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;

public class ImproveBranchCoverageTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private IGrepTool grepTool;
	private ISortTool sorttool;
	private IWcTool wctool;
	
	private String testString = "The quick brown fox jumps over the lazy dog.\n";

	@Before
	public void before() throws IOException {
	}

	@After
	public void after() {
	}
	
	/*
	 * New grep tests
	 */

	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void allOptionsExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "-A", "2", "-B", "1", "-C", "1", "-cov", "2", file.getName() });
		assertEquals("1\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void noNumberArgExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "-A" });
		assertEquals("-A requires a positive number\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void negativeNumberArgExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "-A", "-1" });
		assertEquals("-A requires a positive number\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void insufficientArgExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] {});
		assertEquals("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void illegalArgExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "-D" });
		assertEquals("Illegal argument -D\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void stdinExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "1" });
		assertEquals("1\n", grepTool.execute(folder.getRoot(), "1\n2\n3\n4\n\5\n"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void stdinNullExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "1" });
		assertEquals("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]\n", grepTool.execute(folder.getRoot(), null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void stdinDashExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "1", "-" });
		assertEquals("1\n", grepTool.execute(folder.getRoot(), "1\n2\n3\n4\n\5\n"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void twoStdinDashExecuteTest() throws IOException {
		File file = folder.newFile();
		Files.write(file.toPath(), "1\n2\n3\n4\n\5\n".getBytes());
		grepTool = new GrepTool(new String[] { "1", "-", "-" });
		assertEquals("1\n", grepTool.execute(folder.getRoot(), "1\n2\n3\n4\n\5\n"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.GrepTool#execute(java.io.File, java.lang.string)}.
	 */
	@Test
	public void helpExecuteTest() throws IOException {
		grepTool = new GrepTool(new String[] { "-help" });
		assertEquals(String.format("grep [-cov] [-A num] [-B num] [-C num] [pattern] [file ...]%n"), grepTool.execute(null, null));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/*
	 * New sort tests
	 */
	
	@Test
	public void executeHelpTest() throws IOException {
		sorttool = new SortTool(new String[] { "-help" });
		String result = sorttool.execute(null, null);
		assertTrue(result.startsWith("NAME\n\nsort - sort lines of text files\n\n"));
		assertTrue(result.endsWith("-help\tBrief information about supported options\n"));
		assertTrue(result.contains("DESCRIPTION\n\nWrite sorted concatenation of all FILE(s)"));
		assertTrue(result.contains("-c\tCheck whether the given file is already sorted, if it"));
		assertEquals(0, sorttool.getStatusCode());
	}
	
	@Test
	public void executeStdinTest() throws IOException {
		sorttool = new SortTool(new String[] {});
		String result = sorttool.execute(null, "");
		assertEquals("", result);
		assertEquals(0, sorttool.getStatusCode());
	}
	
	@Test
	public void executeTwoStdinDashTest() throws IOException {
		sorttool = new SortTool(new String[] { "-",  "-" });
		String result = sorttool.execute(null, "");
		assertEquals("", result);
		assertEquals(0, sorttool.getStatusCode());
	}
	
	@Test
	public void executeStdinDashTest() throws IOException {
		sorttool = new SortTool(new String[] { "-" });
		String result = sorttool.execute(null, "");
		assertEquals("", result);
		assertEquals(0, sorttool.getStatusCode());
	}
	
	@Test
	public void executeCheckIfSortedTest() throws IOException {
		sorttool = new SortTool(new String[] { "-c", "a", "b" });
		String result = sorttool.execute(null, "");
		assertEquals(String.format("sort: extra operand `b' not allowed with -c%n"), result);
		assertEquals(1, sorttool.getStatusCode());
	}
	
	/*
	 * New uniq tests
	 */
	
	@Test
	public void endToEndHelp() {
		UniqTool tool = new UniqTool(new String[]{"-help"});
		String result = tool.execute(null, null);
		assertTrue(result.contains("uniq"));
	}
	
	@Test
	public void endToEndNullStdin() {
		UniqTool tool = new UniqTool(new String[]{"-"});
		String result = tool.execute(null, null);
		assertTrue(result.equals(""));
	}
	
	@Test
	public void endToEndEmptyStdin() {
		UniqTool tool = new UniqTool(new String[]{"-"});
		String result = tool.execute(null, "");
		assertTrue(result.equals(""));
	}
	
	@Test
	public void endToEndParamsIncludingHelp() {
		UniqTool tool = new UniqTool(new String[]{"-i", "-help", "-f", "2"});
		String result = tool.execute(null, "");
		assertTrue(result.contains("uniq"));
	}
	
	@Test
	public void endToEndMissingParam() {
		UniqTool tool = new UniqTool(new String[]{"-f"});
		tool.execute(null, null);
		assertNotEquals(0, tool.getStatusCode());
	}
	
	/*
	 * New wc tests
	 */

	@Test
	public void executeNoStdinWcTest() throws IOException {
		wctool = new WcTool(new String[] {});
		String msg = wctool.execute(null, null);
		assertEquals(String.format("usage: wc [-lmw] [file ...]%n"), msg);
		assertTrue(wctool.getStatusCode() == 0);
	}
	
	@Test
	public void executeStdinWcTest() throws IOException {
		wctool = new WcTool(new String[] {});
		String msg = wctool.execute(null, testString);
		assertEquals("1\t9\t45\t" + System.lineSeparator(), msg);
	}
	
	@Test
	public void executeStdinDashWcTest() throws IOException {
		wctool = new WcTool(new String[] { "-" });
		String msg = wctool.execute(null, testString);
		assertEquals("1\t9\t45\t" + System.lineSeparator(), msg);
	}
	
	@Test
	public void executeTwoStdinDashWcTest() throws IOException {
		wctool = new WcTool(new String[] { "-", "-" });
		String msg = wctool.execute(null, testString);
		assertEquals("1\t9\t45\t" + System.lineSeparator(), msg);
	}
	
	/*
	 * New cat tests
	 */
	
	@Test
	public void executeCatDoubleStdin() {
		ICatTool tool = new CatTool(new String[]{"-", "-"});
		String expected = "This is stdin!";

		String result = tool.execute(null, expected);

		assertEquals(expected, result);
		assertEquals(0, tool.getStatusCode());
	}

	/*
	 * New cd tests
	 */

	@Test
	public void toolExecuteNoArgs() throws IOException {
		CdTool tool = new CdTool(new String[0], new Shell(System.in, System.out));
		String result = tool.execute(new File(System.getProperty("user.dir")), "");
		assertEquals("", result);
		assertNotEquals(0, tool.getStatusCode());
	}
	
	@Test
	public void toolExecuteBadArgs() throws IOException {
		CdTool tool = new CdTool(new String[]{null}, new Shell(System.in, System.out));
		String result = tool.execute(new File(System.getProperty("user.dir")), "");
		assertEquals("", result);
		assertNotEquals(0, tool.getStatusCode());
	}
	
	/*
	 * New echo tests
	 */
	
	/**
	 * MUT: echo()
	 * Should return parameter passed.
	 */
	@Test
	public void echoTest2() {
		IEchoTool tool = new EchoTool(new String[0]);
		String expected = "\n";
		String result = tool.execute(null, null);
		assertEquals(expected, result);
		assertEquals(tool.getStatusCode(), 0);
	}
	
	/*
	 * New move tests
	 */
	
	@Test
	public void moveToolBadArgs() {
		IMoveTool tool = new MoveTool(new String[0]);
		String result = tool.execute(null, null);
		assertEquals("", result);
		assertEquals(1, tool.getStatusCode());
	}
	
	/*
	 * New copy tests
	 */
	
	private byte[] fileGetContents(File f) {
		if (f.exists() && f.canRead()) {
			try {
				int read;
				byte[] buf = new byte[4096];
				FileInputStream is = new FileInputStream(f);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((read = is.read(buf)) != -1) {
					baos.write(buf, 0, read);
				}
				is.close();
				return baos.toByteArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	private String fileGetString(File f) {
		byte[] result = fileGetContents(f);
		if (result == null) {
			return null;
		} else {
			return new String(result, StandardCharsets.UTF_8);
		}
	}

	/**
	 * MUT: copy()
	 * Source file should not be blank.
	 * @throws IOException
	 */
	@Test
	public void sameSrcAndDest() throws IOException {
		File f = new File("testSrc");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(new FileOutputStream(f));
		pw.write("test string.");
		pw.close();

		File dest = new File("testSrc");

		ICopyTool copyTool = new CopyTool(new String[0]);
		boolean result = copyTool.copy(f, dest);
		String contents = fileGetString(dest);
		assertEquals("test string.", contents);
		assertTrue(result);
		assertEquals(0, copyTool.getStatusCode());

		f.delete();
	}
}
