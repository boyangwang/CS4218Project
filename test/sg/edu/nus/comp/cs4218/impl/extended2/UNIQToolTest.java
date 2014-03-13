package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class UNIQToolTest {
	private UniqTool uniqtool;

    /**
     * @CORRECTED
     * @throws Exception
     */
	@Before
	public void setUp() throws Exception {
        // Nothing here.
	}

	@After
	public void tearDown() throws Exception {
        uniqtool = null;
	}

    private UniqTool defaultTool() {
        return new UniqTool(new String[0]);
    }

    /**
     * @CORRECTED
     */
	@Test
	public void testGetHelpTest() {
		String result = defaultTool().getHelp();
		assertTrue(result.startsWith("NAME\n\nuniq - report or omit repeated lines\n"));
		assertTrue(result.contains("-f NUM\tSkips NUM fields on each line before checking for uniqueness"));
		assertTrue(result.contains("separated from each other by at least one space or"));
        assertTrue(result.endsWith("-help\tBrief information about supported options.\n"));
    }

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueOneLineTest() {
        uniqtool = defaultTool();
		String input = "hello world\n";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("hello world\n"),0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("hello world\n"), 0);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueDifferentCaseTest() {
        uniqtool = defaultTool();
		String input = "hello world\nHELLo WOrld";
		String result = uniqtool.getUnique(true, input);
		assertTrue(result.equals("hello world\nHELLo WOrld\n"));
		result = uniqtool.getUnique(false, input);
		assertTrue(result.equals("hello world\n"));
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueManySameLineTest() {
        uniqtool = defaultTool();
		String input = "test1\nhello\nhello\nhello\nHello\ntest1";
		String result = uniqtool.getUnique(true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\ntest1\n"), 0);
		result = uniqtool.getUnique(false, input);
		assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSpecialCharacterTest() {
        uniqtool = defaultTool();
		String input = "test1\nhello\nHello\\n好\n";
		String result = uniqtool.getUnique(true, input);

        // Original assert was wrong.
//        assertEquals(result.compareTo("test1\nhello\nHello\\n好\n"), 0);

        assertTrue(result.equals("test1\nhello\nHello\\n好\n"));
		result = uniqtool.getUnique(false, input);
        // Original assert was wrong.
//        assertEquals(result.compareTo("test1\nhello\n好\n"), 0);
        assertTrue(result.equals("test1\nhello\nHello\\n好\n"));
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumOneLineTest() {
        uniqtool = defaultTool();
		String input = "hello world\n";
		String result = uniqtool.getUniqueSkipNum(1,true, input);
		assertEquals(result.compareTo("hello world\n"),0);
		result = uniqtool.getUniqueSkipNum(12,false, input);
		assertEquals(result.compareTo("hello world\n"), 0);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumDifferentCaseTest() {
        uniqtool = defaultTool();
		String input = "hello world\nhello world\nHELLo WorlD";
		String result = uniqtool.getUniqueSkipNum(5,true, input);

        // Original assert does not conform to project specifications.
//        assertEquals(result.compareTo("hello world\nHELLo WorlD\n"),0);

        assertTrue(result.equals("hello world\n"));
		result = uniqtool.getUniqueSkipNum(5,false, input);

        // Original assert does not conform to project specifications.
//        assertEquals(result.compareTo("hello world\n"), 0);

		assertTrue(result.equals("hello world\n"));
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumManySameLineTest() {
        uniqtool = defaultTool();
		String input = "test1\nhello\nhello\nhello\nHello\ntest1";
		String result = uniqtool.getUniqueSkipNum(2, true, input);

        // Original assert does not conform to project specifications.
//		assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);

        assertTrue(result.equals("test1\n"));
        result = uniqtool.getUniqueSkipNum(2, false, input);

        // Original assert does not conform to project specifications.
//        assertEquals(result.compareTo("test1\nhello\ntest1\n"), 0);

        assertTrue(result.equals("test1\n"));
    }

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumSpecialCharacterTest(){
        uniqtool = defaultTool();
		String input = "test1\nhello\nHello\n叉烧包\n";
		String result = uniqtool.getUniqueSkipNum(2, false, input);

        // Original assert does not conform to project specifications.
//		assertEquals(result.compareTo("test1\nhello\n叉烧包\n"), 0);

        assertTrue(result.equals("test1\n"));
        result = uniqtool.getUniqueSkipNum(2, true, input);

        // Original assert does not conform to project specifications.
//		assertEquals(result.compareTo("test1\nhello\n叉烧包\n"), 0);

        assertTrue(result.equals("test1\n"));
    }

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumMinNumTest() {
        uniqtool = defaultTool();
		String input = "test1\nhello\nHello\n叉烧包\n豆沙包\n";
		String result = uniqtool.getUniqueSkipNum(0, false, input);
		assertEquals(result.compareTo("test1\nhello\n叉烧包\n豆沙包\n"), 0);
		result = uniqtool.getUniqueSkipNum(0, true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\n叉烧包\n豆沙包\n"), 0);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void getUniqueSkipNumMaxNumTest(){
        uniqtool = defaultTool();
		String input ="test1\nhello\nHello\n叉烧包\n豆沙�?";
		String result = uniqtool.getUniqueSkipNum(20, false, input);
		assertEquals(result.compareTo("test1\n"), 0);
		result = uniqtool.getUniqueSkipNum(20, true, input);
		assertEquals(result.compareTo("test1\n"), 0);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void executeNonNumTest(){
        String[] args = new String[]{"-i", "-f", "a", "text.txt"};
		uniqtool = new UniqTool(args);
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "");
		assertTrue(result.equals("Error: NUM has to be a positive number."));
		assertEquals(uniqtool.getStatusCode(),1);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void executeNegNumTest(){
        String[] args = new String[]{"-i", "-f", "-2", "text.txt"};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "");
        assertTrue(result.equals("Error: NUM has to be a positive number."));
		assertEquals(uniqtool.getStatusCode(),1);
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String fileContents = " \nHello World\nhello World\nTest\ntest\njest\nBEST";
		Files.write(file.toPath(), fileContents.getBytes(), StandardOpenOption.CREATE);
        String[] args = new String[]{"-f", "2", "-f", "3", "-f", "1", file.getName()};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(file.getParentFile(), "");

        // Original assert makes no sense for any interpretation of input arguments.
//		assertEquals(result.compareTo("Hello World\nTest\nBEST\n"), 0);

        // The last specified parameter is sticky.
        assertTrue(result.equals(" \nHello World\nTest\n"));

		Files.delete(file.toPath());
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionIgnoreCaseTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String fileContents = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
		Files.write(file.toPath(), fileContents.getBytes(),  StandardOpenOption.CREATE);
        String[] args = new String[]{"-f", "2", "-i", "-f", "3", "-f", "1", file.getName()};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(file.getParentFile(), "");

        // Original assert makes no sense for any interpretation of input arguments.
//		assertEquals(result.compareTo("Hello World\nTEST\n"), 0);

        // The last specified parameter is sticky.
        assertTrue(result.equals(" \nHello World\nTEST\n"));
		Files.delete(file.toPath());
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeGetUniqueTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String fileContents = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), fileContents.getBytes(),  StandardOpenOption.CREATE);
        String[] args = new String[]{file.getName()};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(file.getParentFile(), "");
		assertEquals(result.compareTo(" \nHello World\nhello World\nTest\ntest\n"), 0);
		Files.delete(file.toPath());
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeGetUniqueIgnoreCaseTest() throws IOException{
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTEST\ntest";
		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
        String[] args = new String[]{"-i", file.getName()};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(file.getParentFile(), "");
		assertEquals(result.compareTo(" \nHello World\nTEST\n"), 0);
		Files.delete(file.toPath());
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeEmptyFileTest() throws IOException {
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
        String[] args = new String[]{"-f", "2", "-i", "-f", "3", "-f", "1", file.getName()};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(file.getParentFile(), "");
		assertEquals("\n", result);
		Files.delete(file.toPath());
	}

    /**
     * @CORRECTED
     * @throws IOException
     */
	@Test
	public void executeInvalidOptionTest() throws IOException {
        String[] args = new String[]{"-f", "2", "-i", "-f", "3", "-f", "1", "-a", "temp.txt"};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "");
		assertEquals("Error: Invalid option.", result);
		assertEquals(uniqtool.getStatusCode(),1);
	}

    /**
     * @CORRECTED
     */
	@Test
	public void executeInvalidFileNameTest() {
        String[] args = new String[]{"-f", "2", "-i", "-f", "3", "-f", "1", "temp.txt"};
        uniqtool = new UniqTool(args);
		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")), "");
		assertEquals("Error: FILE is not found.", result);
		assertEquals(uniqtool.getStatusCode(), 1);
	}

    // The following nonsensical tests have been removed.

//	@Test
//	public void executeFileStdinWithOptionTest() throws IOException {
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTest\ntest";
//		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
//		input = "-i " + file.getName() + " -";
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals("Hello World\nTest\n", result);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeFileStdinNoOptionTest() throws IOException {
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTest\ntest";
//		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
//		input = " " + file.getName() + " -";
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals("Hello World\nhello World\nTest\ntest\n", result);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeFolderTest() throws IOException{
//		File dir = Files.createTempDirectory("temp dir").toFile();
//		String result = uniqtool.execute(dir.getParentFile(), dir.getName());
//		assertEquals(result,"Error: FILE is not found");
//		assertEquals(uniqtool.getStatusCode(),1);
//		Files.delete(dir.toPath());
//	}

    // These are our own tests.

    private File tempDir() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Tool should accept data from stdin.
     */
    @Test
    public void accpetsDataFromStdin() {
        String[] args = new String[]{};
        uniqtool = new UniqTool(args);
        String result = uniqtool.execute(tempDir(), "test");

        assertTrue(result.equals("test\n"));
        assertEquals(uniqtool.getStatusCode(), 0);
    }

    /**
     * Tool should prefer data from file when both inputs are given.
     * Data from stdin is ignored.
     * @throws IOException
     */
    @Test
    public void prefersDataFromFile() throws IOException {
        // Temp file.
        String fileData = "file";
        File file = Files.createTempFile("tempFile", ".tmp").toFile();
        Files.write(file.toPath(), fileData.getBytes(),  StandardOpenOption.CREATE);

        String[] args = new String[]{file.getName()};
        uniqtool = new UniqTool(args);
        String result = uniqtool.execute(file.getParentFile(), "stdin");

        assertTrue(result.equals("file\n"));
        assertEquals(uniqtool.getStatusCode(), 0);
    }

    @Test
    public void stdinSupportsArguments() {
        String[] args = new String[]{"-i", "-f", "1"};
        uniqtool = new UniqTool(args);
        String result = uniqtool.execute(tempDir(), "i hello\nb Hello\ntest\nc world");

        assertTrue(result.equals("i hello\ntest\nc world\n"));
        assertEquals(uniqtool.getStatusCode(), 0);
    }
}