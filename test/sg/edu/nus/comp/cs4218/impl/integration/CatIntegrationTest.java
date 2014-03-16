package sg.edu.nus.comp.cs4218.impl.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CatIntegrationTest {
    // Used to abstract Shell testing. You'll need these in a new suite.
    private ByteArrayInputStream simIn;
    private ByteArrayOutputStream simOut;
    private Shell shell;

    // Used only in this test suite.
    private final String TEST_INPUT_FILE = "cat-test-in.txt";
    private final String TEST_OUTPUT_FILE = "cat-test-output.txt";
    private File fin;
    private File fout;

    // You need this method.
    private String getStringFromOutput() {
        String out = new String(simOut.toByteArray(), StandardCharsets.UTF_8);
        out = out.substring(out.indexOf("> ") + 2).trim();
        out = out.substring(0, out.lastIndexOf("\n"));
        return out;
    }

    // You need this method.
    private void setupShellWithInput(String input) {
        // Terminate at the end of this command.
        input += "\r\nctrl-c\r\n";

        simIn = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        shell = new Shell(simIn, simOut);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // You need this line.
        simOut = new ByteArrayOutputStream();

        // Only for this test suite/
        fin = new File(TEST_INPUT_FILE);
        fout = new File(TEST_OUTPUT_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // Only for this test suite.
        if (fin.exists()) {
            fin.delete();
        }
        if (fout.exists()) {
            fout.delete();
        }
    }

    /**
     * cat | grep
     */
    @Test
    public void catGrep() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("cat %s | grep hello", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "hello\nhello";
        assertEquals(expected, result);
    }
    
    /**
     * grep | cat
     */
    @Test
    public void grepCat() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("grep hello %s | cat", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "hello\nhello";
        assertEquals(expected, result);
    }
    
    /**
     * grep | cat | grep | grep | cat | cat
     */
    @Test
    public void grepCatGrepGrepCatCat() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("grep hello %s | cat | grep h | grep e | cat | cat", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "hello\nhello";
        assertEquals(expected, result);
    }
    
    /**
     * cat | grep | cat | sort
     */
    @Test
    public void catGrepCatSort() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog\nHello";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("cat %s | grep l | cat | sort", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "Hello\nhello\nhello\nworld";
        assertEquals(expected, result);
    }
    
    /**
     * cat | grep | cat | sort | wc
     */
    @Test
    public void catGrepCatSortWc() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog\nHello";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("cat %s | grep l | cat | sort | wc", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "4\t4\t24\t";
        assertEquals(expected, result);
    }
}
