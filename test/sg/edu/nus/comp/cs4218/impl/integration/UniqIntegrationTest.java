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
        String[] lines = out.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Ignore empty lines.
            if (line == "") {
                continue;
            }

            // Greedily strip prompts from beginning of line.
            int promptIndex;
            if ((promptIndex = line.lastIndexOf("> ")) >= 0) {
                line = line.substring(promptIndex + 2);
            }

            // Pack line back into string.
            sb.append(line).append("\n");
        }

        // Should not contain trailing newline.
        return sb.toString().trim();
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
     * uniq | cut
     */
    @Test
    public void uniqCut() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | cut -c1-3", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "hel\nwor\ncat\ndog";
        assertEquals(expected, result);
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
        setupShellWithInput(String.format("uniq %s | uniq", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        // Do your asserts.
        String expected = "hello\nworld\ncat\ndog";
        assertEquals(expected, result);
    }

    /**
     * uniq | paste
     * @throws IOException
     */
    @Test
    public void uniqPaste() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        // TODO: paste a file.
        setupShellWithInput(String.format("uniq %s | paste %s", TEST_INPUT_FILE, TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "hello\nhello\nworld\ncat\ndog";
        assertEquals(expected, result);
    }

    /**
     * uniq | wc
     * @throws IOException
     */
    @Test
    public void uniqWc() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | wc", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "4\t4\t20";
        assertEquals(expected, result);
    }

    /**
     * uniq | paste | grep
     * @throws IOException
     */
    @Test
    public void uniqPasteGrep() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | paste %s | grep cat", TEST_INPUT_FILE, TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "cat";
        assertEquals(expected, result);
    }

    /**
     * uniq | wc | grep
     * @throws IOException
     */
    @Test
    public void uniqWcGrep() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | wc | grep 4", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "4\t4\t20";
        assertEquals(expected, result);
    }

    /**
     * uniq | cut | grep
     * @throws IOException
     */
    @Test
    public void uniqCutGrep() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | cut -c1-3 | grep wor", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "wor";
        assertEquals(expected, result);
    }

    /**
     * uniq | uniq | grep
     * @throws IOException
     */
    @Test
    public void uniqUniqGrep() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("uniq %s | uniq | grep dog", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "dog";
        assertEquals(expected, result);
    }

    /**
     * cd
     * uniq
     * @throws IOException
     */
    @Test
    public void stateChangeTest() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        String TEST_DIR = "uniq-test-dir";
        File dir = new File(TEST_DIR);
        dir.mkdir();

        setupShellWithInput(String.format("cd %s\r\nuniq %s", TEST_DIR, TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "";
        assertEquals(expected, result);

        dir.delete();
    }
}
