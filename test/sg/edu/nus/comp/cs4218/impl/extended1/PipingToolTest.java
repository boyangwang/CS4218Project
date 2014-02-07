/**
 * 
 */
package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

/**
 * @author moyang
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
		Shell shell = new Shell();
		String[] args;
		String output;
		
		args = new String[]{"echo foo", "cat -"};
		pipingTool = new PipingTool(args);
		pipingTool.setShell(shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals("foo\n", output);
		
		args = new String[]{"echo foo", "echo bar", "cat -"};
		pipingTool = new PipingTool(args);
		pipingTool.setShell(shell);
		output = pipingTool.execute(shell.getWorkingDirectory(), "");
		assertEquals("bar\n", output);
		
		
	}

	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#pipe(sg.edu.nus.comp.cs4218.ITool, sg.edu.nus.comp.cs4218.ITool)}.
	 */
	@Test
	public void testPipeIToolITool() {
		pipingTool = new PipingTool(new String[]{});
		Shell shell = new Shell();
		pipingTool.setShell(shell);
		
		EchoTool from = new EchoTool(new String[]{"foo"});
		CatTool to = new CatTool(new String[]{"-"});
		
		String output = pipingTool.pipe(from, to);
		assertEquals("foo\n", output);
	}

	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#setShell()}.
	 */
	@Test
	public void testSetShell() {
		pipingTool = new PipingTool(new String[]{});
		Shell shell = new Shell();
		
		// before set shell, should throw exception
		try {
			pipingTool.pipe("foo", new EchoTool(new String[]{}));
			fail("Should throw NullPointerException");
		}
		catch(NullPointerException e) {
			
		}
		catch(Exception e) {
			fail("Should throw NullPointerException, not other types of exceptions");
		}
		
		// after setShell should be fine
		pipingTool.setShell(shell);
		try {
			pipingTool.pipe("foo", new EchoTool(new String[]{}));
			assertTrue("No exception after setShell call", true);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail("Should no throw Exception"); 
		}
	}
	
	/**
	 * Test method for {@link sg.edu.nus.comp.cs4218.impl.extended1.PipingTool#pipe(java.lang.String, sg.edu.nus.comp.cs4218.ITool)}.
	 */
	@Test
	public void testPipeStringITool() {
		pipingTool = new PipingTool(new String[]{});
		Shell shell = new Shell();
		pipingTool.setShell(shell);
		
		String input;
		String result;
		CatTool catTool = new CatTool(new String[]{"-"});
		
		// normal case where the output should be the input string.
		// Assuming EchoTool functional
		input = "foo";
		result = pipingTool.pipe(input, catTool);
		assertEquals(input, result);
		
		input = "'\"\"'";
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		input = "    \n\t";
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		input = "'\"";
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		// pipe operator wraped inside quotes should be supported
		input = "\"|\"";
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		input = "'|'";
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		// some UNICODE testing
		input = "" + Character.toChars(2202).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		input = "" + Character.toChars(0x2E97).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
		
		input = "" + Character.toChars(0x3042).toString();
		result = pipingTool.pipe(input, catTool);
		assertEquals(result, input);
	}

}