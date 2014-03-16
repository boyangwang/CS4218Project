package sg.edu.nus.comp.cs4218.impl.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class UniqIntegrationTest {
    // Used to abstract Shell testing. You'll need these in a new suite.
    private ByteArrayInputStream simIn;
    private ByteArrayOutputStream simOut;
    private Shell shell;

    // Used only in this test suite.
    private final String TEST_INPUT_FILE = "uniq-test-in.txt";
    private final String TEST_OUTPUT_FILE = "uniq-test-output.txt";
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
     * uniq | uniq
     */
    @Test
    public void uniqUniq() throws IOException {
        // Setup the test case.
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // Call and run the shell like this.
        setupShellWithInput(String.format("uniq %s", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "hello\nworld\ncat\ndog";
        assertEquals(expected, result);
    }
}
