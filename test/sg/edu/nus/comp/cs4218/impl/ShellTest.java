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
	}

	@After
	public void tearDown() throws Exception {
		sh = null;
	}

    /**
     * MUT: parse(cmd)
     * Should return echo tool for command with leading spaces
     */
	@Test
	public void parseCommandLeadingSpaces() {
		sh = new Shell(System.in, System.out);
		String cmd = "  echo you shall not pass  ";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool for command with wrong casing
     */
	@Test
	public void parseCommandWrongCase() {
		sh = new Shell(System.in, System.out);
		String cmd = "eCho you shall not pass";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool for command with improperly closing quotes
     */
	@Test
	public void parseCommandWrongQuote() {
		sh = new Shell(System.in, System.out);
		String cmd = "echo 'you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool for command with improperly closing dquotes
     */
	@Test
	public void parseCommandWrongDQuote() {
		sh = new Shell(System.in, System.out);
		String cmd = "echo \"you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool for command with improperly closing quotes
     */
	@Test
	public void parseCommandWrongBQuote() {
		sh = new Shell(System.in, System.out);
		String cmd = "echo `you shall not pass ";
		ITool result = sh.parse(cmd);
		assertEquals(null,result);
	}
    /**
     * MUT: parse(cmd)
     * Should return pipe tool for normal command 
     */
	@Test
	public void parseCommandPipe(){
		sh = new Shell(System.in, System.out);
		String cmd = " echo pipe | cat";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IPipingTool);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool for commnad with pipe char in a quote
     */
	@Test
	public void parseCommandWithQuote(){
		sh = new Shell(System.in, System.out);
		String cmd = "echo `don't seperate | echo us`";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
    /**
     * MUT: parse(cmd)
     * Should return echo tool in normal command parsing 
     */
	@Test
	public void parseNormalCommand() {
		sh = new Shell(System.in, System.out);
		String cmd = "echo you shall not pass";
		ITool result = sh.parse(cmd);
		assertTrue(result instanceof IEchoTool);
	}
    /**
     * MUT: execute(tool)
     * should return a non null executor
     */

	@Test 
	public void executeNormalCommand(){
		sh = new Shell(System.in, System.out);
		ITool tool = sh.parse("echo you shall not pass");
		Runnable executor = sh.execute(tool);
		assertNotEquals(null,executor);
	}
    /**
     * MUT: execute(tool)
     * should return a null executor when executing null
     */
	@Test 
	public void executeNull(){
		sh = new Shell(System.in, System.out);
		Runnable executor = sh.execute(null);
		assertEquals(null,executor);
	}

    /**
     * MUT: execute(tool)
     * should return a null executor when executing null pipe
     */
	@Test 
	public void executeNullPipe(){
		sh = new Shell(System.in, System.out);
		ITool tool = sh.parse("|");
		Runnable executor = sh.execute(tool);
		assertNotEquals(null,executor);
	}
	
}