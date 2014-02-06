package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;


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

	@Test
	public void parseCommandLeadingSpaces() {
		sh = new Shell();
		String cmd = "  echo you shall not pass  ";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test
	public void parseCommandWrongCase() {
		sh = new Shell();
		String cmd = "eCho you shall not pass";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
	@Test
	public void parseCommandWrongQuote() {
		sh = new Shell();
		String cmd = "echo 'you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
	@Test
	public void parseCommandWrongDQuote() {
		sh = new Shell();
		String cmd = "echo \"you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
	@Test
	public void parseCommandWrongBQuote() {
		sh = new Shell();
		String cmd = "echo `you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
	@Test
	public void parseCommandPipe(){
		sh = new Shell();
		String cmd = " echo pipe | cat";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IPipingTool);
	}
	@Test
	public void parseCommandWithQuote(){
		sh = new Shell();
		String cmd = "echo `don't seperate | echo us`";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test
	public void parseNormalCommand() {
		sh = new Shell();
		String cmd = "echo you shall not pass";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
	@Test 
	public void executeNormalCommand(){
		sh = new Shell();
		ITool tool = sh.parse("echo you shall not pass");
		Runnable executor = sh.execute(tool);
		assertNotEquals(null,executor);
	}
	@Test 
	public void executeNull(){
		sh = new Shell();
		Runnable executor = sh.execute(null);
		assertEquals(null,executor);
	}
	
}