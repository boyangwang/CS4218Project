package sg.edu.nus.comp.cs4218.impl;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.IShell;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.PwdTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

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
			handleOutput(_tool.execute(_shell, _cwd, _stdin));
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
    private Shell() {
        // TODO: Initialise instance variables.    	
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
    	Shell shell = Shell.instance();    		
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));		
		Thread runningThread = null;
		System.out.print(cwd.getAbsolutePath() + "> ");
    	while(true){
    		try {    						
				String cmd=buffer.readLine().trim();
				if(cmd.trim().equals("ctrl-z")){
					if (null != runningThread && runningThread.isAlive()){
						shell.stop(runningThread);
					}	
				} else {
					ITool tool = shell.parse(cmd);
					if ((null == runningThread || !runningThread.isAlive()) && tool!=null){
						runningThread = (Thread) shell.execute(tool);
					}else{
						System.out.print(cwd.getAbsolutePath() + "> ");
					}
				}				
					
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

	@Override
	public ITool parse(String commandline) {
		// at the beginning of Shell.parse, if pipe operator is present, pass to PipingTool
		if (isContainsPipeOperator(commandline)) {
			IPipingTool pipingTool = new PipingTool(null, commandline);
			return pipingTool;
		}
		
		if(commandline.trim().startsWith("pwd")){
			return new PwdTool(null, "");
		} else {
			//TODO Implement all other tools
			Logging.logger().writeLog(Logging.Error, "Cannot parse " + commandline);
			return null;
		}
	}

    @Override
	public Runnable execute(ITool tool) {
		// TODO stdin, do piping
		Thread t;
		if (!(tool instanceof IPipingTool)) {
			t = new Thread(new TaskExecution(this, tool, cwd, "", System.out));
		}
		else {
			t = new Thread(new TaskExecution(this, tool, cwd, "", ((IPipingTool)tool).getOutputStream()));
		}
		
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

    
    /**
     * Code for static stuff.
     */
    private static Shell self = null;

	public static void main(String[] args){
        Logging.logger(System.out).setLevel(Logging.All);
		Shell.instance().run();
	}

    public static Shell instance() {
        while (self == null) {
            try {
                self = new Shell();
            } catch (Exception ex) {
                Logging.logger(System.out).writeLog(Logging.Fatal, "Could not instantiate shell.");
                System.exit(1);
            }
        }
        return self;
    }
    
    /**
	 * Parse inputString and returns true if the control should be 
	 * passed to PipingTool
	 * 
	 * @param inputString the user input string
	 * @return true if the control should be passed to PipingTool
	 */
	public static boolean isContainsPipeOperator(String inputString) {
		return inputString.contains("|");
	}
	
}
