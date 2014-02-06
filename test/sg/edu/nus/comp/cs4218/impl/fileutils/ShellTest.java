package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.fileutils.IEchoTool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class ShellTest {

	IShell sh;
	@Before
	public void setUp() throws Exception {
		sh = new Shell();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*TODO:
	 * test execute, parse and stop
	 */
	/*TODO:
	 * 
	 */
	@Test
	public void parseCommandLeadingSpaces() {
		String cmd = "  echo you shall not pass  ";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test
	public void parseCommandWrongCase() {
		String cmd = "eCho you shall not pass";
		ITool result = sh.parse(cmd);
		assertEquals(result,null);
	}
	@Test
	public void parseCommandWrongQuote() {
		String cmd = "echo 'you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(result, null);
	}
	@Test
	public void parseCommandWrongDQuote() {
		String cmd = "echo \"you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(result, null);
	}
	@Test
	public void parseCommandWrongBQuote() {
		String cmd = "echo `you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(result, null);
	}
	@Test
	public void parseCommandPipe(){
		String cmd = " echo pipe | cat";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IPipingTool);
	}
	@Test
	public void parseCommandWithQuote(){
		String cmd = "echo `don't seperate | echo us`";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test
	public void parseNormalCommand() {
		String cmd = "echo you shall not pass";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test 
	public void executeNormalCommand(){
		ITool tool = sh.parse("echo you shall not pass");
		Runnable executor = sh.execute(tool);
		assertNotEquals(executor,null);
	}
	@Test 
	public void executeNull(){
		Runnable executor = sh.execute(null);
		assertEquals(executor,null);
	}
	@Test
	public void stopThread(){
		ITool tool = sh.parse("echo you shall not pass");
		Runnable executor = sh.execute(tool);
		assertNotEquals(executor,null);
		sh.stop(executor);
		
	}
	
}