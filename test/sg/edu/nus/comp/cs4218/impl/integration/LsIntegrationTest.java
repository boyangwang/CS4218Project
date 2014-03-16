package sg.edu.nus.comp.cs4218.impl.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs4218.impl.Shell;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class LsIntegrationTest {
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


		testDir = new File("inteTestLsFold");
		testDir.mkdir();
		//create new file with text
		testFile1 = new File(testDir, "ls1");
		testFile1.createNewFile();
		FileWriter fw = new FileWriter(testFile1.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		StringBuilder sb = new StringBuilder();
		sb.append("Mark Smith\n");
		sb.append("Bobby Brown\n");
		sb.append("Sue Miller\n");
		sb.append("Jenny Igotit");
		bw.write(sb.toString());
		bw.close();

		testFile2 = new File(testDir, "ls2");
		testFile2.createNewFile();
		fw = new FileWriter(testFile2.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		sb = new StringBuilder();
		sb.append("555-1234\n");
		sb.append("555-9876\n");
		sb.append("555-6743\n");
		sb.append("867-5309");
		bw.write(sb.toString());
		bw.close();


		// create new file with text
		testFile3 = new File(testDir, "ls3");
		testFile3.createNewFile();
		fw = new FileWriter(testFile3.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		sb = new StringBuilder();
		sb.append("Mark Smith\n");
		sb.append("Bobby Brown\n");
		sb.append("Sue Miller\n");
		bw.write(sb.toString());
		bw.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

        File[] fileList = testDir.listFiles();
        for (int i=0; i<fileList.length; i++){
            File file = fileList[i];
            file.delete();
        }
        // delete directory
        testDir.delete();

		if (fin.exists()) {
			fin.delete();
		}

		if (fout.exists()) {
			fout.delete();
		}
	}

	/**
	 * ls | grep
	 */
	@Test
	public void lsPipeGrep() throws IOException {

		setupShellWithInput("ls inteTestLsFold | grep ls | sort ");
		shell.run();
		String result = getStringFromOutput();

		String expected = "ls1\nls2\nls3";
		assertEquals(expected, result);
	}

	/**
	 * ls | grep
	 */
	@Test
	public void lsPipeGrepNonExistent() throws IOException {

		setupShellWithInput("ls inteTestLsFold | grep nonexist");
		shell.run();
		String result = getStringFromOutput();

		String expected = "";
		assertEquals(expected, result);
	}
}
