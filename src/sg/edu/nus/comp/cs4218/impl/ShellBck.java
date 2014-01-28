package sg.edu.nus.comp.cs4218.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.impl.fileutils.PWDTool;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * shell can be implemented in Java
 */
public class ShellBck implements IShell {	
	File _workingDir = null;
	
	
	
	
	@Override	
	public ITool parse(String commandline) {
		commandline = commandline.trim();		
				
		if(commandline.startsWith("pwd")){
			return new PWDTool();
		} else {
			//TODO Implement all other tools
			System.err.println("Cannot parse " + commandline);
			//TODO: dummy result 
			return new PWDTool();
			//return null;
		}
	}

	@Override
	public Runnable execute(ITool tool) {
		// TODO Implement

		return null;
	}

	@Override
	public void stop(Runnable toolExecution) {
		//TODO: assumption Runnable implemented by Thread		
		if (toolExecution instanceof Thread){
			((Thread) toolExecution).interrupt();
		}
	}

	private static class ToolExecuter implements Runnable {
		private ATool _tool = null;
		private File _workingDir = null;
		private String _stdin = null;
		public ToolExecuter(ATool tool, File workingDir, String stdin){
			_tool = tool;
			_workingDir = workingDir;
			_stdin = stdin;
		}
		@Override
		public void run() {
			
			if(_tool != null){
				_tool.execute(_workingDir, _stdin);
			}
			
		}
		
	}
	/**
	 * Do Forever
     * 1. Wait for a user input
     * 2. Parse the user input. Separate the command and its arguments
     * 3. Create a new thread to execute the command
     * 4. Execute the command and its arguments on the newly created thread. Exit with the status code of the executed command
     * 5. In the shell, wait for the thread to complete execution
     * 6. Report the exit status of the command to the user
	 */
	public static void main(String[] args){
		//TODO Implement		
		ShellBck shell = new ShellBck();
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));		
		Runnable runningThread = null;
		Queue<ITool> cmdQueue=new LinkedList<ITool>();
		
		while(true){						
			try {
				System.out.print("Enter command > ");
				String cmd=buffer.readLine();
				if(cmd.trim().equals("ctrl-z")){
					if (runningThread != null){
						shell.stop(runningThread);
					}					
				}else{
					ITool tool = shell.parse(cmd);
					if(tool!=null){
						cmdQueue.add(tool);											
					}
				}
				//TODO: this will make shell execute next command only after user 
				//enters something				
				if (runningThread!=null && cmdQueue.size()!=0 ){
					runningThread = shell.execute(cmdQueue.poll());
				}
				
				
				
				
				
				
				/*if(cmd.trim().equals("ctrl-z")){
					shell.stop(runningThread);
				}
				ITool tool = shell.parse(cmd);
				if (tool == null){			
					//error here, do what now?
				}else{
					cmdQueue.add(tool);
					if (cmdQueue.size()>0){
						shell.execute(cmdQueue.poll());
					}				
				}*/				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
	        
		}
	}	
}
