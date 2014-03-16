package sg.edu.nus.comp.cs4218.impl.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.assertEquals;

public class ShellIntegrationTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

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
     * State of the Shell scenario 1
     */
    @Test
    public void shellStateSequenceOne() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("copy %s b\r\nmove b c\r\ncat c\r\ndelete c", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "";
        assertEquals(expected, result);
    }
    
    /**
     * State of the Shell scenario 2
     */
    @Test
    public void shellStateSequenceTwo() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("copy %s b\r\ncopy b d\r\nmove b c\r\ncat c\r\ndelete c\r\ndelete d", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "";
        assertEquals(expected, result);
    }
    
    /**
     * State of the Shell scenario 3
     */
    @Test
    public void shellStateSequenceThree() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("copy %s b\r\nmove b c\r\ncat c\r\ndelete c\r\necho s", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "s";
        assertEquals(expected, result);
    }
    
    /**
     * State of the Shell scenario 4
     */
    @Test
    public void shellStateSequenceFour() throws IOException {
        String input = "hello\nhello\nworld\ncat\ndog";
        FileWriter fw = new FileWriter(fin);
        fw.write(input);
        fw.close();

        setupShellWithInput(String.format("copy %s b\r\nmove b c\r\ncat c\r\ndelete c\r\ncd ..", TEST_INPUT_FILE));
        shell.run();
        String result = getStringFromOutput();

        String expected = "";
        assertEquals(expected, result);
    }
}
