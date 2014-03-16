package sg.edu.nus.comp.cs4218.impl.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class EchoIntegrationTest {
	private final String TEST_INPUT_FILE = "uniq-test-in.txt";
	private final String TEST_OUTPUT_FILE = "uniq-test-output.txt";

	private ByteArrayInputStream simIn;
	private ByteArrayOutputStream simOut;
	private Shell shell;
	private File fin;
	private File fout;

	private File testDir;
	private File testFile1;
	private File testFile2;
	private File testFile3;

	private String getStringFromOutput() {
		String out = new String(simOut.toByteArray(), StandardCharsets.UTF_8);
		out = out.substring(out.indexOf("> ") + 2).trim();
		if (out.lastIndexOf("\n")>-1){
			out = out.substring(0, out.lastIndexOf("\n"));
		}else{
			out = "";
		}
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

		fin = new File(TEST_INPUT_FILE);
		fout = new File(TEST_OUTPUT_FILE);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {


		if (fin.exists()) {
			fin.delete();
		}

		if (fout.exists()) {
			fout.delete();
		}
	}

	/**
	 * echo | cut
	 */
	@Test
	public void echoSpacePipeCut() throws IOException {

		setupShellWithInput("echo '123456789 987654321' | cut -c-5,10");
		shell.run();
		String result = getStringFromOutput();

		String expected = "12345 \n";
		assertEquals(expected, result);
	}
	/**
	 * echo | cut
	 */
	@Test
	public void echoPipeCut() throws IOException {

		setupShellWithInput("echo 123456789 | cut -c1-2,5- ");
		shell.run();
		String result = getStringFromOutput();

		String expected = "1256789\n";
		assertEquals(expected, result);
	}

}
