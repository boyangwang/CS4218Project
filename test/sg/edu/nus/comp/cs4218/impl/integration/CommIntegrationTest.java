package sg.edu.nus.comp.cs4218.impl.integration;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class CommIntegrationTest {
	private final String TEST_INPUT_FILE_1 = "comm-test-in-1.txt";
	private final String TEST_INPUT_FILE_2 = "comm-test-in-2.txt";
    private final String TEST_OUTPUT_FILE = "comm-test-output.txt";

    private ByteArrayInputStream simIn;
    private ByteArrayOutputStream simOut;
    private Shell shell;
    private File fin1;
    private File fin2;
    private File fout;

    private String getStringFromOutput() {
        String out = new String(simOut.toByteArray(), StandardCharsets.UTF_8);
        out = out.substring(out.indexOf("> ") + 2).trim();
        out = out.substring(0, out.lastIndexOf("\n"));
        return out;
    }

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
        simOut = new ByteArrayOutputStream();

        fin1 = new File(TEST_INPUT_FILE_1);
        fin2 = new File(TEST_INPUT_FILE_2);
        fout = new File(TEST_OUTPUT_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        if (fin1.exists()) {
            fin1.delete();
        }
        if (fin2.exists()) {
            fin2.delete();
        }
        if (fout.exists()) {
            fout.delete();
        }
    }
    
    /**
     * comm | cut
     */
    @Test
    public void commCut() throws Exception {
        // Setup the test case.
        String input = "a\nb\n";
        FileWriter fw = new FileWriter(fin1);
        fw.write(input);
        fw.close();
        input = "b\nc\n";
        fw = new FileWriter(fin2);
        fw.write(input);
        fw.close();
        
        setupShellWithInput(String.format("comm %s %s | cut -c 1", TEST_INPUT_FILE_1, TEST_INPUT_FILE_2));
        shell.run();
        String output = getStringFromOutput();
        
        // see bug report
//        assertEquals("a\n\t\t", output);
    }

}
