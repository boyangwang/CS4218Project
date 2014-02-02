package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.ReverseTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PwdTool;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * The Shell is used to interpret and execute user's
 * commands. Following sequence explains how a basic
 * instance can be implemented in Java
 */
public class Shell implements IShell {
	/**
	 * TaskExecution Thread
	 */
	private static class TaskExecution implements Runnable{
		ITool _tool;
		File _cwd;
		String _stdin;
		IShell _shell;
		OutputStream _stdout;
		public TaskExecution(IShell shell, ITool tool, File cwd, String stdin, OutputStream stdout){
			_shell = shell;
			_tool = tool;
			_cwd = cwd;
			_stdin = stdin;
			_stdout = stdout;
		}		
		@Override
		public void run() {			
			handleOutput(_tool.execute(_shell, _cwd));
			System.out.print(_cwd.getAbsolutePath() + "> ");
			
		}
		
		public void handleOutput(String output) {
			if (_stdout instanceof PrintStream) {
				((PrintStream)_stdout).println(output);
			}
			else {
				try {
					_stdout.write(output.getBytes());
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
    /**
     * Code for instance stuff.
     */
    private File cwd = null;    
    public Shell() {
    	String userDir = System.getProperty("user.dir");
    	cwd = new File(userDir);
    }

    /**
     * Do Forever
     * 1. Wait for a user input
     * 2. Parse the user input. Separate the command and its arguments
     * 3. Create a new thread to execute the command
     * 4. Execute the command and its arguments on the newly created thread. Exit with the status code of the executed command
     * 5. In the instance, wait for the thread to complete execution
     * 6. Report the exit status of the command to the user
     */    
	public void run() {
        Scanner sc = new Scanner(System.in);
		Thread runningThread = null;

        System.out.print(cwd.getAbsolutePath() + "> ");
        while (true) {
            String cmd = sc.nextLine().trim();

            if (cmd.equals("ctrl-z")) {
                if (null != runningThread && runningThread.isAlive()) {
                    stop(runningThread);
                }
            } else {
                ITool tool = parse(cmd);
                if ((runningThread == null || !runningThread.isAlive()) && tool != null) {
                    runningThread = (Thread)execute(tool);
                } else {
                    System.out.print(cwd.getAbsolutePath() + "> ");
                }
            }
        }
    }

	@Override
	public ITool parse(String commandline) {
		// at the beginning of Shell.parse, if pipe operator is present, pass to PipingTool
		if (isContainsPipeOperator(commandline)) {
			String[] args = new String[1];
			args[0] = commandline;
			IPipingTool pipingTool = new PipingTool(args, "");
			return pipingTool;
		}
		 
		String[] argsWithCmdName = tokenizeCommandlineString(commandline);
		String[] args = getArgsFromCommandline(argsWithCmdName);
		
		if(commandline.trim().startsWith("pwd")){
			
			return new PwdTool(null, "");
		} 
		else if(commandline.trim().startsWith("reverse")) {
			
			return new ReverseTool(args, "");
		}
		else {
			//TODO Implement all other tools
			Logging.logger(System.out).writeLog(Logging.Error, "Cannot parse " + commandline);
			return null;
		}
	}

	@Override
	public Runnable execute(ITool tool) {
		// TODO stdin, do piping
		Thread t;
//		if (!(tool instanceof IPipingTool)) {
			t = new Thread(new TaskExecution(this, tool, cwd, "", System.out));
//		}
//		else {
//			t = new Thread(new TaskExecution(this, tool, cwd, "", ((IPipingTool)tool).getOutputStream()));
//		}
		
		t.start();
		return t;
	}

	@Override
	public void stop(Runnable toolExecution) {
		if (toolExecution instanceof Thread){
			Thread t = (Thread) toolExecution;
			t.interrupt();
		}
	}

    /**
     * Direct access to working directory for cd tool.
     *
     * Pre-condition: newDirectory has been validated.
     *
     * @param newDirectory The new working directory.
     */
    public void changeWorkingDirectory(File newDirectory) {
        this.cwd = newDirectory;
    }
    
    public File getWorkingDirectory() {
    	return this.cwd;
    }

    
    /**
     * Code for static stuff.
     */
	public static void main(String[] args){
        Logging.logger(System.out).setLevel(Logging.All);
		Shell sh = new Shell();
        sh.run();
	}
    
    /**
	 * Parse inputString and returns true if the control should be 
	 * passed to PipingTool
	 * 
	 * @param inputString the user input string
	 * @return true if the control should be passed to PipingTool
	 */
	private static boolean isContainsPipeOperator(String inputString) {
		return inputString.contains("|");
	}
	
	private String[] tokenizeCommandlineString(String str) {
		// all crappy for the time being code
		return str.split(" ");
	}
	
    private String[] getArgsFromCommandline(String[] argsWithCmdName) {
    	String[] args = new String[argsWithCmdName.length-1];
    	for (int i=1; i<argsWithCmdName.length; i++) {
    		args[i-1] = argsWithCmdName[i];
    	}
    	
		return args;
	}

}
