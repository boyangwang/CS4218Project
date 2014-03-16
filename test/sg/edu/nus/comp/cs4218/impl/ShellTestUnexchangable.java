/* NOTE:
 * These test cases are made for our project only and not to be exchanged.
 */
package sg.edu.nus.comp.cs4218.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.ITool;

public class ShellTestUnexchangable {
	IShell sh;
	@Before
	public void setUp() throws Exception {
		sh = new Shell(System.in, System.out);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void stopThreadTest(){
		sh = new Shell(System.in, System.out);
		String[] args = {};
		ITool tool = (ITool) new TestInterruptTool(args);

		Runnable executor = sh.execute(tool);
		sh.stop(executor);
		try {
			//allow the other thread to end
			Thread.sleep(500); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(1,tool.getStatusCode());
	}
	
	private class TestInterruptTool extends ATool implements ITool{
		public TestInterruptTool(String[] arguments) {
			super(arguments);
		}
		@Override
		public String execute(File workingDir, String stdin) {
			setStatusCode(0);
			while(!Thread.currentThread().isInterrupted()){
			}
			setStatusCode(1);
			return null;
		}
	}
}
