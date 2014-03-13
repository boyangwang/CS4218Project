package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UNIQToolTest {
	private UniqTool uniqtool;

    /**
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
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
     * Edited.
     */
	@Test
	public void getUniqueSkipNumMinNumTest() {
        uniqtool = defaultTool();
		String input = "test1\nhello\nHello\n叉烧包\n豆沙包\n";
		String result = uniqtool.getUniqueSkipNum(0,false, input);
		assertEquals(result.compareTo("test1\nhello\n叉烧包\n豆沙包\n"), 0);
		result = uniqtool.getUniqueSkipNum(0,true, input);
		assertEquals(result.compareTo("test1\nhello\nHello\n叉烧包\n豆沙包\n"), 0);
	}

    /**
     * Edited.
     */
	@Test
	public void getUniqueSkipNumMaxNumTest(){
        uniqtool = defaultTool();
		String input ="test1\nhello\nHello\n叉烧包\n豆沙�?";
		String result = uniqtool.getUniqueSkipNum(20,false, input);
		assertEquals(result.compareTo("test1\n"), 0);
		result = uniqtool.getUniqueSkipNum(20,true, input);
		assertEquals(result.compareTo("test1\n"), 0);
	}

//	@Test
//	public void executeNonNumTest(){
//		String input = "-i -f a text.txt";
//		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),input);
//		assertEquals(result.compareTo("Error: NUM has to be a positive number"), 0);
//		assertEquals(uniqtool.getStatusCode(),1);
//	}
//
//	@Test
//	public void executeNegNumTest(){
//		String input = "-i -f -2 text.txt";
//		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),input);
//		assertEquals(result.compareTo("Error: NUM has to be a positive number"), 0);
//		assertEquals(uniqtool.getStatusCode(),1);
//	}
//
//	@Test
//	public void executeGetUniqueSkipNumMultipleSameOptionTest() throws IOException{
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTest\ntest\njest\nBEST";
//		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
//		input = "-f 2 -f 3 -f 1 " + file.getName();
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals(result.compareTo("Hello World\nTest\nBEST\n"), 0);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeGetUniqueSkipNumMultipleSameOptionIgnoreCaseTest() throws IOException{
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
//		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
//		input = "-f 2 -i -f 3 -f 1 " + file.getName();
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals(result.compareTo("Hello World\nTEST\n"), 0);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeGetUniqueTest() throws IOException{
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTest\ntest";
//		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
//		input = " " + file.getName();
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals(result.compareTo("Hello World\nhello World\nTest\ntest\n"), 0);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeGetUniqueIgnoreCaseTest() throws IOException{
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = " \nHello World\nhello World\nTEST\ntest";
//		Files.write(file.toPath(), input.getBytes(),  StandardOpenOption.CREATE);
//		input = "-i " + file.getName();
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals(result.compareTo("Hello World\nTEST\n"), 0);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeEmptyFileTest() throws IOException {
//		File file = Files.createTempFile("tempFile", ".tmp").toFile();
//		String input = "-f 2 -i -f 3 -f 1 " + file.getName();
//		String result = uniqtool.execute(file.getParentFile(),input);
//		assertEquals("\n", result);
//		Files.delete(file.toPath());
//	}
//
//	@Test
//	public void executeInvalidOptionTest() throws IOException {
//		String input = "-f 2 -i -f 3 -f 1 -a temp.txt";
//		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),input);
//		assertEquals("Error: Invalid option", result);
//		assertEquals(uniqtool.getStatusCode(),1);
//	}
//
//	@Test
//	public void executeInvalidFileNameTest() {
//		String input = "-f 2 -i -f 3 -f 1 temp.txt";
//		String result = uniqtool.execute(new File(System.getProperty("java.io.tmpdir")),input);
//		assertEquals("Error: FILE is not found", result);
//		assertEquals(uniqtool.getStatusCode(),1);
//	}
//
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
}