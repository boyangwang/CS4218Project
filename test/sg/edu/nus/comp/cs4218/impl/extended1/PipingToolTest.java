/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

/**
 *
 */
public class PipingToolTest {
	private PipingTool pipingTool;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		pipingTool = null;
	}

	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#execute(java.io.File, java.lang.String)}.
	 */
	@Test
	public void testExecute() {
		Shell shell = new Shell(System.in, System.out);
		String[] args;
		String output;

		args = new String[]{"echo foo", "cat -"};
		pipingTool = new PipingTool(args, shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals("foo" + System.lineSeparator(), output);
		
		args = new String[]{"echo foo", "echo bar", "cat -"};
		pipingTool = new PipingTool(args, shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals("bar" + System.lineSeparator(), output);
		
		// cases where one or more of the commands are null
		args = new String[]{"echo foo", ""};
		pipingTool = new PipingTool(args, shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals(PipingTool.ERROR_MSG_NULL_CMD + System.lineSeparator(), output);
		
		args = new String[]{"", "echo foo"};
		pipingTool = new PipingTool(args, shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals(PipingTool.ERROR_MSG_NULL_CMD + System.lineSeparator(), output);
		
		args = new String[]{"echo foo", "", ""};
		pipingTool = new PipingTool(args, shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals(1, pipingTool.getStatusCode());
		assertEquals(PipingTool.ERROR_MSG_NULL_CMD + System.lineSeparator(), output);
	}

	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#pipe(sg.edu.nus.comp.cs4218.ITool, sg.edu.nus.comp.cs4218.ITool)}.
	 */
	@Test
	public void testPipeIToolITool() {
		pipingTool = new PipingTool(new String[]{}, new Shell(System.in, System.out));
		
		EchoTool from = new EchoTool(new String[]{"foo"});
		CatTool to = new CatTool(new String[]{"-"});
		
		String output = pipingTool.pipe(from, to);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals("foo" + System.lineSeparator(), output);
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#pipe(java.lang.String, sg.edu.nus.comp.cs4218.ITool)}.
	 */
	@Test
	public void testPipeStringITool() {
		pipingTool = new PipingTool(new String[]{}, new Shell(System.in, System.out));
		
		String input;
		String result;
		CatTool catTool = new CatTool(new String[]{"-"});
		
		// normal case where the output should be the input string.
		// Assuming EchoTool functional
		input = "foo";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(input, result);
		
		input = "'\"\"'";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		input = "    \n\t";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		input = "'\"";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		// pipe operator wraped inside quotes should be supported
		input = "\"|\"";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		input = "'|'";
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		// some UNICODE testing
		input = "" + Character.toChars(2202).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		input = "" + Character.toChars(0x2E97).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
		
		input = "" + Character.toChars(0x3042).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(0, pipingTool.getStatusCode());
		assertEquals(result, input);
	}

}
